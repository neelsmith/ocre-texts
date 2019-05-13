import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source

import edu.holycross.shot.mid.validator._

import edu.holycross.shot.ocre._


// alphabetized list of unique whitespace-delimited tokens
def wordList(c: edu.holycross.shot.ohco2.Corpus) : Vector[String] = {
  val txts = c.nodes.map(_.text)
  txts.map(_.split(" ")).toVector.flatten.distinct.sorted
}

def profileCorpus(c: edu.holycross.shot.ohco2.Corpus): Unit = {
  val txts = c.nodes.map(_.text)
  val tokens = txts.map(_.split(" ")).toVector.flatten

  println("Tokens: " + tokens.size)
  println("Distinct tokens: " + tokens.distinct.size)
}
