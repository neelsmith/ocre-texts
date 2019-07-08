package edu.holycross.shot.ocre

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.tabulae._

import scala.io.Source

//WORKS ON VECTORES OF ANALYZED TOKENS!
//
/** A class for working with a vector of analyzed tokens,
* as part of a syntactic formula.
*
* @param lexTokens Vector of lexical tokens.
*/
case class FormulaUnit(lexTokens: Vector[SecondaryAnalysis])  {

  def nouns : Vector[SecondaryAnalysis] = lexTokens.filter(
    t => t match {
      case lex: LexicalAnalysis =>  lex.tkn.nounToken
      case _ => false
    }
  )

  def verbs : Vector[SecondaryAnalysis] = lexTokens.filter(
    t => t match {
      case lex: LexicalAnalysis =>  lex.tkn.verbToken
      case _ => false
    }
  )
  def adjs : Vector[SecondaryAnalysis] = lexTokens.filter(
    t => t match {
      case lex: LexicalAnalysis =>  lex.tkn.adjToken
      case _ => false
    }
  )
  def ptcpls : Vector[SecondaryAnalysis] = lexTokens.filter(
    t => t match {
      case lex: LexicalAnalysis =>  lex.tkn.ptcplToken
      case _ => false
    }
  )

/*
  def verbs : Vector[AnalyzedToken] = lexTokens.filter(_.verbToken)
  def adjs : Vector[AnalyzedToken] = lexTokens.filter(_.adjToken)
  def ptcpls : Vector[AnalyzedToken] = lexTokens.filter(_.ptcplToken)

  def nounPattern = nouns.distinct



  def nounCluster(n: AnalyzedToken) : Vector[AnalyzedToken] =  nouns.filterNot(_ == n).filter(noun => FormulaUnit.substsAgree(n,noun))
*/

  //def nounsClustered = FormulaUnit.substCluster(nouns.head, nouns.tail)
}

object FormulaUnit {


  // read in FST parse of corpus and convert to AnalyzedTokens
  def parses(fstFile: String): Vector[AnalyzedToken] = {
    val fstLines = Source.fromFile(fstFile).getLines.toVector
    val analyzedTokens = FstFileReader.parseFstLines(fstLines)
    analyzedTokens
  }

  def tokens(c: Corpus, allParses: Vector[AnalyzedToken]) : Vector[SecondaryAnalysis] = {
    val tkns = for (n <- c.nodes) yield { tokens(n, allParses)}
    tkns.flatten
  }

  // look up anlayzed tokens for all tokens in a citable node
  def tokens(n: CitableNode, allParses: Vector[AnalyzedToken]) : Vector[SecondaryAnalysis] = {
    val tkns = NormalizedLegendOrthography.tokenizeNode(n) // yields a Vector[edu.holycross.shot.mid.validator.MidToken]
    val secondaryAnalyses = for (tkn <- tkns) yield {
      val secondaryAnalysis = tkn.tokenCategory.get match {
        case LexicalToken => {
          val parses = allParses.filter(_.token == tkn.string)
          parses.size match {
            case 1 => LexicalAnalysis(tkn.string, tkn.tokenCategory.get, parses(0))
            case 0 => throw new Exception("FormulaUnit: Exception.  Did not find parse for lexical token " + tkn.string)
            case _ => throw new Exception(s"FormulaUnit: Exception.  Found ${parses.size} parses for ${tkn.string}.")
          }
        }
        case NumericToken =>  NumericAnalysis(tkn.string, tkn.tokenCategory.get)
      }
      secondaryAnalysis
    }
    secondaryAnalyses.toVector
  }
}


/*
  def fus(analyzedCorpus: Vector[Vector[AnalyzedToken]]): Vector[FormulaUnit] = {
    Vector.empty[FormulaUnit]
  }

  // true if 2 substantive (can) have same GCN
  def substsAgree(s1: AnalyzedToken, s2: AnalyzedToken): Boolean = s1.gcn.toSet.intersect(s2.gcn.toSet).nonEmpty

*/


/*
  def substCluster(subst: AnalyzedToken, substVect: Vector[AnalyzedToken], clusters:  Vector[]) = {
    if (substVect.isEmpty) {

    }
    val matchingSubsts = substVect.filterNot(_ == subst).filter(subst2 => substsAgree(subst,subst2))
    val remainder = substVect.filterNot(matchingSubsts.contains(_))

  }*/
