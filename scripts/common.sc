//
// Shared common functions:
//
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source
import edu.holycross.shot.latin.Latin23Alphabet
import edu.holycross.shot.mid.validator._

def loadCorpus : Corpus = {
  println("Loading corpus...")
  val cex = Source.fromFile("ocre-data/raw.cex").getLines.mkString("\n")
  val c = Corpus(cex)
  println("Done.")
  c
}

// Get alphabetized list of all characters in this corpus.
def rawAlphabet(c: Corpus = loadCorpus) =  {
  c.nodes.map(_.text).mkString("").distinct.sorted
}

/*

def validString(s: String, generous: Boolean = false) : Boolean = {
  val correct = "[A-Z_\\.• ]*"
  val expandedList = "[A-Z_\\.• \\]\\[\\)\\(]*"
  if (generous) {
    s.matches(expandedList)
  } else {
    s.matches(correct)
  }
}

// stand in for orthography
def validText(corpus: Corpus, generous: Boolean = false) : Corpus = {
  val validNodes = corpus.nodes.filter( n => validString(n.text, generous))
  Corpus(validNodes)
}

def badText(corpus: Corpus, generous: Boolean = false) : Corpus = {
  val validNodes = corpus.nodes.filterNot( n => validString(n.text, generous))
  Corpus(validNodes)
}

def tokenCounts(corpus: Corpus) : Vector[(CitableNode, Int)]= for (n <- corpus.nodes) yield { (n, Latin23Alphabet.tokenizeNode(n).size) }


def obverse(tokens  : Vector[MidToken]) = {
  tokens.filter( tkn => tkn.urn.passageComponent.contains(".obv.") )
}

def reverse(tokens  : Vector[MidToken]) = {
  tokens.filter( tkn => tkn.urn.passageComponent.contains(".rev.") )
}
*/
/*

//Get initial numbers
val tokens = Latin23Alphabet.tokenizeCorpus(corpus)

val textCount = corpus.size
val tokensCount = tokens.size
val charCount = corpus.nodes.map(_.text.size).sum

val messageCount = corpus.nodes.map(_.text).distinct.size

get histogram of disinct messages!

*/
/*

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

println("\n\nLoad corpus of texts:")
println("\n\tval corpus = loadCorpus")
