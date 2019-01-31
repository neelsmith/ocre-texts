package edu.holycross.shot.ocre

import edu.holycross.shot.ocre._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source

import edu.holycross.shot.mid.validator._

import java.io.PrintWriter

object OcreUtilities {


  /** Load an OHCO2 Corpus from a 2-column CEX file.
  *
  * @param cexFile Name of file to load corpus from.
  */
  def loadCorpus(cexFile: String = "ocre-data/raw.cex") : Corpus = {
    println("Loading corpus...")
    val cex = Source.fromFile(cexFile).getLines.mkString("\n")
    val c = Corpus(cex)
    println("Done.")
    c
  }

  // Get alphabetized list of all characters in this corpus.
  def rawAlphabet(c: Corpus = loadCorpus()) =  {
    c.nodes.map(_.text).mkString("").distinct.sorted
  }

  def profileCorpus(c: Corpus, normalized: Boolean = true) : Unit = {
    print(s"${c.size} coin legends;  ")
    // distinct messages
    val messages = c.nodes.map(_.text).distinct.size
    println(s"${messages} distinct legends.")
    // token histogram
    val tokens = if (normalized) {
      NormalizedLegendOrthography.tokenizeCorpus(c)
    } else {
      DiplomaticLegendOrthography.tokenizeCorpus(c)
    }
    print(s"${tokens.size} tokens;  ")
    val tokenStrings = tokens.map(_.string)
    println(s"${tokenStrings.distinct.size} distinct tokens.")

    val chars = rawAlphabet(c)
    print(c.nodes.map(_.text).mkString("").size + " characters;  ")
    println(s"${chars.size} distinct characters.")
  }


  def profileIssuer(c: Corpus) = {
    val parts = c.nodes.map(n => n.urn.passageComponent.split("\\."))
    val issuers = parts.map( v => s"${v(0)}-${v(1)}")
    val grouped = issuers.groupBy(s => s)
    val counts = grouped.toSeq.map{ case (k,v) => (k, v.size) }
    val sorted = counts.sortBy(_._2).reverse
  }


  def tokenCounts(corpus: Corpus, normalized: Boolean = true) : Vector[(CitableNode, Int)]= {
    val tokens = if (normalized) {
      for (n <- corpus.nodes) yield { (n, NormalizedLegendOrthography.tokenizeNode(n).size) }
    } else {
      for (n <- corpus.nodes) yield { (n, DiplomaticLegendOrthography.tokenizeNode(n).size) }
    }
    tokens
  }


  def obverse(c  : Corpus) = {
    Corpus(c.nodes.filter(_.urn.passageComponent.contains(".obv.") ))
  }

  def reverse(c  : Corpus) = {
    Corpus(c.nodes.filter(_.urn.passageComponent.contains(".rev.") ))
  }

  def validText(c: Corpus, normalized: Boolean = true) = {
    normalized match {

      case true => {
        for (n <- c.nodes) yield {
          (n, NormalizedLegendOrthography.validString(n.text))
        }
      }
      case false => {
        for (n <- c.nodes) yield {
          (n, DiplomaticLegendOrthography.validString(n.text))
        }
      }
    }
  }

  def byTokenCount(corpus: Corpus, n: Int, normalized: Boolean = true) = {
    val counts = tokenCounts(corpus, normalized)
    val filtered = counts.filter(_._2 == n).map(_._1)
    Corpus(filtered)
  }

  def messageFreqs(corpus: Corpus) = {
    val grouped = corpus.nodes.groupBy(_.text)
    val counts = grouped.map{ case (msg, v) => (msg, v.size) }
    counts.toSeq.sortBy(_._2).reverse
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

}
