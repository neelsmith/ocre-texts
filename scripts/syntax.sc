import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source

import edu.holycross.shot.mid.validator._

import edu.holycross.shot.ocre._
import edu.holycross.shot.tabulae._

val fst = "newparse.txt"


def parsedForms(f: String = fst): Vector[AnalyzedToken] = {
  val fstLines = Source.fromFile(f).getLines.toVector
  val analyzedTokens = FstFileReader.parseFstLines(fstLines)
  analyzedTokens
}

// get orthographically valid entries from a CEX file
def good (src: String = "ocre-data/normalized15.cex") = {
  val corpus = OcreUtilities.loadCorpus(src)
  OcreUtilities.goodOnly(corpus)
}


// get reverse legends from a corpus
def rev(c: Corpus) = {
  Corpus(c.nodes.filter(_.urn.passageComponent.contains(".rev.")))
}


def dropString(n: CitableNode,s : String) : CitableNode = {
  val trimmed = n.text.replaceAll(s,"")
  CitableNode(n.urn, trimmed)
}

def revSyntax(n: CitableNode) ={
  val trimmed = dropString(n, "senatvs consvlto")
  //println("Trimmed to " + trimmed)
  val revTkns = NormalizedLegendOrthography.tokenizeNode(trimmed)
  println(revTkns.mkString("\n"))
}
