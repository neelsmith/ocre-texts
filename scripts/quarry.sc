///import edu.holycross.shot.nomisma._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source


import edu.holycross.shot.latin.Latin23Alphabet
import edu.holycross.shot.mid.validator._


// Counts by RIC volume:
// val ricVol = c.nodes.map(_.urn.passageComponent.split("\\.")).map( _(0))
// val grouped = ricVol.groupBy(i => i).map{ case (k,v) => (k, v.size) }
// println(grouped.toSeq.sortBy(_._1).mkString("\n"))




/*

//Get initial numbers
val tokens = Latin23Alphabet.tokenizeCorpus(corpus)

val textCount = corpus.size
val tokensCount = tokens.size
val charCount = corpus.nodes.map(_.text.size).sum

val messageCount = corpus.nodes.map(_.text).distinct.size

get histogram of disinct messages!

*/


def bin(tokenCounts: Vector[(String,Int)]): Vector[(Int, Int)]= {
  val grouped = tokenCounts.groupBy(_._2)
  val binned = grouped.map{ case (k, v) => (k, v.size) }
  binned.toSeq.sortBy(_._1).toVector
}

def histCsv(intPairs:  Vector[(Int, Int)]): String = {
  val lines = for (pair <- intPairs) yield {
    s"${pair._1},${pair._2}"
  }
  lines.mkString("\n")
}

/*
val grouped = tokenCounts.groupBy(_._2)
val binned = grouped.map{ case (k, v) => (k, v.size) }

 val bitokes = corpus.nodes.filter( n => Latin23Alphabet.tokenizeNode(n).size == 2)

 val bitokeObv = bitokes.filter( c => {val parts = c.urn.passageComponent.split("\\."); parts(parts.size - 2) == "obv"} )

val msgs = corpus.nodes.map(_.text).distinct
val tokenSizes =  msgs.map(s => {val tkns = s.split("\\s+"); (s, tkns.size) })
Eliminte entries with multiple legends!
val legit  = tokenSizes.filter(_._1.contains(" or ") == false)
*/

println("\n\nLoad corpus of texts:")
println("\n\tval corpus = loadCorpus")
