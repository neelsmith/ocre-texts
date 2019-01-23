///import edu.holycross.shot.nomisma._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source

def loadCorpus : Corpus = {
  println("Loading corpus...")
  val cex = Source.fromFile("ocre-data/corpus.cex").getLines.mkString("\n")
  val c = Corpus(cex)
  println("Done.")
  c
}


/*

//Get initial numbers
import edu.holycross.shot.latin.Latin23Alphabet
val tokens = Latin23Alphabet.tokenizeCorpus(corpus)


val textCount = corpus.size
val tokensCount = tokens.size
val charCount = corpus.nodes.map(_.text.size).sum

*/


println("\n\nLoad corpus of texts:")
println("\n\tval corpus = loadCorpus")
