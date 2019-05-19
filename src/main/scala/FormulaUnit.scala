package edu.holycross.shot.ocre
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.tabulae._




//WORKS ON VECTORES OF ANALYZED TOKENS!
//
/** A class for working with a vector of analyzed tokens,
* as part of a syntactic formula.
*
* @param tkns Vector of tokens.
*/
case class FormulaUnit(tkns: Vector[AnalyzedToken])  {


  def nouns : Vector[AnalyzedToken] = tkns.filter(_.nounToken)
  def verbs : Vector[AnalyzedToken] = tkns.filter(_.verbToken)
  def adjs : Vector[AnalyzedToken] = tkns.filter(_.adjToken)
  def ptcpls : Vector[AnalyzedToken] = tkns.filter(_.ptcplToken)

  def nounPattern = nouns.distinct



  def nounCluster(n: AnalyzedToken) : Vector[AnalyzedToken] =  nouns.filterNot(_ == n).filter(noun => FormulaUnit.substsAgree(n,noun))


  //def nounsClustered = FormulaUnit.substCluster(nouns.head, nouns.tail)
}

object FormulaUnit {


  // read in FST parse of corpus and convert to AnalyzedTokens
  def parses(fstFile: String): Vector[AnalyzedToken] = {
    val fstLines = Source.fromFile(fstFile).getLines.toVector
    val analyzedTokens = FstFileReader.parseFstLines(fstLines)
    analyzedTokens
  }

  // look up anlayzed tokens for all tokens in a citable node
  def tokens(n: CitableNode, allParses: Vector[AnalyzedToken]) : Vector[AnalyzedToken] = {
    val tkns = NormalizedLegendOrthography.tokenizeNode(n)
    val legendParses = for (tkn <- tkns) yield {
      //println("Look for " + tkn)
      val parse = allParses.filter(_.token == tkn.string)
      if (parse.isEmpty) {
        println("DID NOT FIND PARSE FOR " + tkn.string)
      }
      parse
    }
    legendParses.flatten
  }

  def fus(analyzedCorpus: Vector[Vector[AnalyzedToken]]): Vector[FormulaUnit] = {
    Vector.empty[FormulaUnit]
  }

  // true if 2 substantive (can) have same GCN
  def substsAgree(s1: AnalyzedToken, s2: AnalyzedToken): Boolean = s1.gcn.toSet.intersect(s2.gcn.toSet).nonEmpty


/*
  def substCluster(subst: AnalyzedToken, substVect: Vector[AnalyzedToken], clusters:  Vector[]) = {
    if (substVect.isEmpty) {

    }
    val matchingSubsts = substVect.filterNot(_ == subst).filter(subst2 => substsAgree(subst,subst2))
    val remainder = substVect.filterNot(matchingSubsts.contains(_))

  }*/
}
