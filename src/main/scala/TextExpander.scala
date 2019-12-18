package edu.holycross.shot.ocre



import scala.io.Source
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

import edu.holycross.shot.nomisma._

object TextExpander  {



  /** URL for default data set.*/
  val defaultUrl = "https://raw.githubusercontent.com/neelsmith/nomisma/master/cex/ric-1-3-cts.cex"

  /** Default corpus: clean data from RIC volumes 1-3.*/
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
  *
  * @param lastInt Last index of file names.  Since Scala sequences
  * are 0-origin, this will be one less than the number of mapping files.
  * @param baseDirectory Directory where conventionally named files
  * are found.
  * @param baeFile File name that will have [[lastInt]] and ".csv"
  * appended to it.
  */
  def loadNMappings(lastIndex: Int, baseDirectory: String = "mappings", baseFile : String = "mappings") : Vector[String] = {

    val nameRange = 0 to lastIndex
    val fileNames = for (i <- nameRange) yield {
      baseDirectory + "/" + baseFile + i + ".csv"
    }
    loadMappings(fileNames.toVector)
  }



  /** Recursively apply mappings to expand diplomatic text.
  *
  * @param corpus Corpus in diplomatic edition to be expanded.
  * @param mappings Mapping strings to apply in order to expand text.
  */
  def expandText(corpus: Corpus, mappings: Vector[String]) : Corpus = {
    if (mappings.isEmpty)   {
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


  /**
  *
  * @param lastIndex Last index of mapping files to use.
  * @param baseDirectory Name of directory with mappings files.
  * @param baeFile Base file name to be expanded with index number.
  * @param initialCorpus Corpus to expand.
  */
  def expandFromMappingsDir(lastIndex: Int, baseDirectory: String,
    baseFile : String = "mappings", initialCorpus: Corpus = defaultCorpus) = {
    val dataLoaded = loadNMappings(lastIndex, baseDirectory, baseFile)
    expandText(initialCorpus, dataLoaded)
  }


  /** Pair each node in a corpus with a boolean indicating
  * whether its orthography is valid within a specified
  * system.
  *
  * @param c Corpus to analyze.
  * @param normalized True if corpus should be analyzed in
  * terms of normalized orthography.  If false, corpus is
  * analyzed in terms of diplomatic orthography.
  */
  def orthoStatus(c: Corpus, normalized: Boolean = true) = {
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

  def validOrtho(c: Corpus, normalized: Boolean = true) = {
    orthoStatus(c, normalized).filter(_._2)
  }

  def invalidOrtho(c: Corpus, normalized: Boolean = true) = {
    orthoStatus(c, normalized).filterNot(_._2)
  }


}
