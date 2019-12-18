/*
// 1. Add maven repository where we can find our libraries
val myBT = coursierapi.MavenRepository.of("https://dl.bintray.com/neelsmith/maven")
interp.repositories() ++=   Seq(myBT)

// 2. Make libraries available with `$ivy` imports:
import $ivy.`edu.holycross.shot::nomisma:2.2.0`
import $ivy.`edu.holycross.shot::ohco2:10.17.0`
import $ivy.`edu.holycross.shot.cite::xcite:4.1.1`
//import $ivy.`edu.holycross.shot::histoutils:2.2.0`
//import $ivy.`org.plotly-scala::plotly-almond:0.7.1`
*/


import scala.io.Source
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

import edu.holycross.shot.nomisma._


val corpusUrl = "https://raw.githubusercontent.com/neelsmith/nomisma/master/cex/ric-1-3-cts.cex"
val initialCorpus = CorpusSource.fromUrl(corpusUrl)


/** Fold in updated text from [[newer]] while maintaining
* document order.
*
* @param older Original corpus.
* @param newer Updated texts.
*/
def merge(older: Corpus, newer: Corpus) : Corpus = {
  val urnList = newer.nodes.map(_.urn)

  val updatedNodes = for (n <- older.nodes) yield {
    if (urnList.contains(n.urn)) {
      val newNodes = newer.nodes.filter(_.urn == n.urn)
      require(newNodes.size == 1, s"Matched wrong number of nodes (${newNodes.size}) for URN " + n.urn)
      newNodes(0)
    } else {
      n
    }
  }
  Corpus(updatedNodes)
}


// Recursively apply mappings
def expandText(corpus: Corpus, mappings: Vector[String]) : Corpus = {
  if (mappings.isEmpty) {
    corpus

  } else {
    val pairs = for (mapping <- mappings.head.split("\n")) yield {
      mapping.split(",")
    }
    val modifiedNodes = for (pair <- pairs) yield {
      val matchingNodes = corpus.nodes.filter(_.text == pair(0))
      for (n <- matchingNodes) yield {
        CitableNode(n.urn, pair(1))
      }
    }

    val modifiedCorpus = Corpus(modifiedNodes.toVector.flatten)

    val newCorpus =  merge(corpus, modifiedCorpus)
    expandText(newCorpus, mappings.tail)
  }
}


// Load mappings from file
def loadMapping(f: String) = {
  Source.fromFile(f).getLines.toVector.mkString("\n")
}

def loadMappings(fileNames: Vector[String], data: Vector[String] = Vector.empty[String]) : Vector[String] = {
  if (fileNames.isEmpty) {
    data
  } else {
    val newData = data ++ Vector(loadMapping(fileNames.head))
    loadMappings(fileNames.tail, newData)
  }
}


def loadNMappings(lastInt: Int) = {
  val base = "mappings/mappings"
  val nameRange = 0 to lastInt
  val fileNames = for (i <- nameRange) yield {
    base + i + ".csv"
  }
  loadMappings(fileNames.toVector)
}


def convertAll(n: Int, corpus: Corpus = initialCorpus) = {
  val dataLoaded = loadNMappings(n)
  expandText(initialCorpus, dataLoaded)
}


val converted = convertAll(21)



def validText(c: Corpus, normalized: Boolean = true) = {
  normalized match {

    case true => {
      for (n <- c.nodes) yield {
        (n, NormalizedOcreOrthography.validString(n.text))
      }
    }
    case false => {
      for (n <- c.nodes) yield {
        (n, DiplomaticOcreOrthography.validString(n.text))
      }
    }
  }
}


val validConverts = validText(converted).filter(_._2)
val badNodes = validText(converted).filterNot(_._2)

println("Valid nodes: " + validConverts.size)
println("Bad nodes: " + badNodes.size)

val badCorpus = Corpus(badNodes.map(_._1))

val noNero = badCorpus.nodes.filterNot(_.urn.passageComponent.startsWith("1_2.ner"))


val badTexts = noNero.map(_.text).distinct

println("Bad legends: " + badTexts.size)

println("Valid converts: " + validConverts.size)

println(validConverts.take(10).mkString("\n"))

// Profile good and bad by RIC volume
