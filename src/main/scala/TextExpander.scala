package edu.holycross.shot.ocre



import scala.io.Source
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

import edu.holycross.shot.nomisma._

object TextExpander  {



  val defaultUrl = "https://raw.githubusercontent.com/neelsmith/nomisma/master/cex/ric-1-3-cts.cex"
  lazy val defaultCorpus = CorpusSource.fromUrl(defaultUrl)



  /** Load  a Vector of mapping strings from a named file.
  *
  * @param f File to load mapping strings from.
  */
  def loadMappingFile(f: String) = {
    Source.fromFile(f).getLines.toVector.mkString("\n")
  }


  /** Recursively load  a Vector of mapping strings from a Vector of named files.
  *
  * @param fileNames Names of files to load mapping strings from.
  * @param data Mapping strings loaded so far.
  */
  def loadMappings(fileNames: Vector[String], data: Vector[String] = Vector.empty[String]) : Vector[String] = {
    if (fileNames.isEmpty) {
      data
    } else {
      println("Loading data from " + fileNames.head)
      val newData = data ++ loadMappingFile(fileNames.head).split("\n").toVector
      //println("New data has " + newData.size + " mappings")
      loadMappings(fileNames.tail, newData)
    }
  }



  /** Create a Vector of file names for conventionally named
  * set of files.
  */
  def loadNMappings(lastInt: Int, baseDirectory: String = "mappings/mappings") = {

    val nameRange = 0 to lastInt
    val fileNames = for (i <- nameRange) yield {
      baseDirectory + i + ".csv"
    }
    loadMappings(fileNames.toVector)
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


  def convertAll(n: Int, initialCorpus: Corpus = defaultCorpus) = {
    val dataLoaded = loadNMappings(n)
    expandText(initialCorpus, dataLoaded)
  }

}
