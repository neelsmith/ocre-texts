import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source

import edu.holycross.shot.mid.validator._

import edu.holycross.shot.ocre._
import edu.holycross.shot.tabulae._

val fst = "newparse.txt"

// read fst output file, create vector of analyzed tokens
def parsedForms(f: String = fst): Vector[AnalyzedToken] = {
  val fstLines = Source.fromFile(f).getLines.toVector
  val analyzedTokens = FstFileReader.parseFstLines(fstLines)
  analyzedTokens
}

val parsed = parsedForms()


case class FormulaUnit(tkn: AnalyzedToken)  {

  // ASSUME ONLY ONE PoS possible, so
  // can take first analysis we find:
  //def pos : String = tkn.analyses(0).posLabel

  // (possiby empty) list of case values; if tkn
  // is a substantive, list should be non-empty
  def substCase : Vector[GrammaticalCase] = {
    if (tkn.analyses.isEmpty) {
      Vector.empty[GrammaticalCase]
    } else {
      val caseList = for (lysis <- tkn.analyses) yield {
        lysis match {
            case n : NounForm => Some(n.grammaticalCase)
            case adj : AdjectiveForm => Some(adj.grammaticalCase)
            case _ => None
        }
      }
      caseList.flatten.toVector.distinct.filterNot(_ == Vocative)
    }
  }

  def substGender: Vector[Gender] = {
    if (tkn.analyses.isEmpty) {
      Vector.empty[Gender]
    } else {
      val genderList = for (lysis <- tkn.analyses) yield {
        lysis match {
            case n : NounForm => Some(n.gender)
            case adj : AdjectiveForm => Some(adj.gender)
            case _ => None
        }
      }
      genderList.flatten.toVector.distinct
    }
  }

  def fnctn: String = {
    ""
  }
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

// trim text from legend of citable node
def dropString(n: CitableNode,s : String) : CitableNode = {
  val trimmed = n.text.replaceAll(s,"")
  CitableNode(n.urn, trimmed)
}


// analyze syntactic formula of a revere legend
def revSyntax(n: CitableNode, allParses: Vector[AnalyzedToken] = parsed) = {
  val trimmed = dropString(n, "senatvs consvlto")
  //println("Trimmed to " + trimmed)
  val revTkns = NormalizedLegendOrthography.tokenizeNode(trimmed)
  val legendParses = for (tkn <- revTkns) yield {
    val parse = allParses.filter(_.token == tkn.string)
    println("Parsed: " + tkn + " => "+ parse.size)
    parse
  }
  legendParses.flatten
}
