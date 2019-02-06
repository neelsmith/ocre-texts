///import edu.holycross.shot.nomisma._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source
import edu.holycross.shot.latin.Latin23Alphabet
import edu.holycross.shot.mid.validator._





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


def ngramming(c: Corpus) = {
   val tknVects = for (n <- c.nodes) yield { NormalizedLegendOrthography.tokenizeNode(n) }
   val sizes = tknVects.map( v => v.size)
   val grouped = sizes.groupBy( i => i)
  grouped.toSeq.map{ case (k,v) => (k, v.size) }
}

/*
println(ngramDists.toSeq.sortBy(_._2).reverse.mkString("\n"))
(2,25398)
(6,14537)
(4,13244)
(3,11709)
(5,8744)
(7,5166)
(8,4070)
(9,3071)
(1,3064)
(11,1535)
(10,1262)
(12,727)
(13,658)
(15,371)
(14,115)
(16,42)
(17,17)
(25,2)
(21,1)


*/


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
