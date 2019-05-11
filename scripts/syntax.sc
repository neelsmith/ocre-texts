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


case class GCNTriple(gender: Gender, gcase: GrammaticalCase, gnumber: GrammaticalNumber)

case class FormulaUnit(tkn: AnalyzedToken)  {

  // ASSUME ONLY ONE PoS possible, so
  // can take first analysis we find:
  def nounToken : Boolean = {
    tkn.analyses(0) match {
      case n: NounForm => true
      case _ => false
    }
  }


  def adjToken : Boolean = {
    tkn.analyses(0) match {
      case n: AdjectiveForm => true
      case _ => false
    }
  }

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


  def grammNumber: Vector[GrammaticalNumber] = {
    if (tkn.analyses.isEmpty) {
      Vector.empty[GrammaticalNumber]
    } else {
      val numberList = for (lysis <- tkn.analyses) yield {
        lysis match {
            case n : NounForm => Some(n.grammaticalNumber)
            case adj : AdjectiveForm => Some(adj.grammaticalNumber)
            //case cverb:
            // case participle:
            case _ => None
        }
      }
      numberList.flatten.toVector.distinct
    }
  }

  def gcn: Vector[GCNTriple] = {
    if (tkn.analyses.isEmpty) {
      Vector.empty[GCNTriple]
    } else {
      val tripleList = for (lysis <- tkn.analyses) yield {
        lysis match {
            case n : NounForm => Some(GCNTriple(n.gender, n.grammaticalCase, n.grammaticalNumber))
            case adj : AdjectiveForm => Some(GCNTriple(adj.gender, adj.grammaticalCase, adj.grammaticalNumber))
            //case cverb:
            // case participle:
            case _ => None
        }
      }
      tripleList.flatten.toVector.distinct
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
  val trimmed = n.text.replaceAll(s," ")
  CitableNode(n.urn, trimmed)
}


def nounAnalysis(nouns: Vector[FormulaUnit] ) = {
  println("\n\n" + nouns.size + " nouns.\n")
  for (n <- nouns) {
    println(n.tkn.token + ": " + n.substGender.mkString(", ") + " : " + n.substCase.mkString(", ") + "\n")
    println("GCN: " + n.gcn)
  }
}

def adjAnalysis(adjs: Vector[FormulaUnit] ) = {
  println("\n\n" + adjs.size + " adjectives.\n")
  for (adj <- adjs) {
    println(adj.tkn.token + ": " + adj.substGender.mkString(", ") + " : " + adj.substCase.mkString(", ") + "\n")
    println("GCN: " + adj.gcn)
  }
}


// True if at least one matching GCN form for noun and ajd
def naAgree(noun: FormulaUnit, adj: FormulaUnit): Boolean = {
  val nForms = noun.gcn.toSet
  val aForms = adj.gcn.toSet
  nForms.intersect(aForms).nonEmpty
}

def nounsAgree(noun1: FormulaUnit, noun2: FormulaUnit): Boolean = {
  val n1Forms = noun1.gcn.toSet
  val n2Forms = noun2.gcn.toSet
  n1Forms.intersect(n2Forms).nonEmpty
}


def nounFormula = {
  // given a clustered sequence of nouns, determine
  // the compositional formula
}

// group together nouns that match in GCN
def clusterNouns = {
  // work though list of nouns.
  // if others agree, eliminate them from consideration.
  // should result in a Vector of Vectors with internal vector
  // containing (possibly) agreeing nouns

}
// analyze syntactic formula of a reverse legend and return parsed
// tokens
def revTokens(n: CitableNode, allParses: Vector[AnalyzedToken] = parsed) : Vector[AnalyzedToken] = {
  val trimmed = dropString(n, "senatvs consvlto")
  //println("Trimmed to " + trimmed)
  val revTkns = NormalizedLegendOrthography.tokenizeNode(trimmed)
  val legendParses = for (tkn <- revTkns) yield {
    println("Look for " + tkn)
    val parse = allParses.filter(_.token == tkn.string)
    if (parse.isEmpty) {
      println("DID NOT FIND PARSE FOR " + tkn.string)
    }
    //println("Parsed: " + tkn + " => "+ parse.size)
    parse
  }
  legendParses.flatten
}

def nounsFromNode(n: CitableNode, allParses: Vector[AnalyzedToken] = parsed)  = {
  val legendParses = revTokens(n, allParses)
  val fus = for (lgnd <- legendParses) yield {
    FormulaUnit(lgnd)
  }
  fus.filter(_.nounToken)
}
def adjsFromNode(n: CitableNode, allParses: Vector[AnalyzedToken] = parsed)  = {
  val legendParses = revTokens(n, allParses)
  val fus = for (lgnd <- legendParses) yield {
    FormulaUnit(lgnd)
  }
  fus.filter(_.adjToken)
}
def revFormula(n: CitableNode, allParses: Vector[AnalyzedToken] = parsed)  = {
  val nouns = nounsFromNode(n, allParses)
  //nounAnalysis(nouns)
  val adjs = adjsFromNode(n, allParses)
  //adjAnalysis(adjs)

  val naPairs = for (n <- nouns) yield {
    val adjMatches = adjs.filter(a => naAgree(n,a)).map(_.tkn.token)
    (n.tkn.token, adjMatches)
  }
  //println("NA PAIRS: " + naPairs)

  println("NOUN PATTERN: " + nouns.size + " nouns.\n\t" + nouns.map(_.substCase))
}

def revFormulas(c: Corpus, allParses: Vector[AnalyzedToken] = parsed)  = {
  for (n <- c.nodes) {

  }
}
