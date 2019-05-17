package edu.holycross.shot.ocre.syntax

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.tabulae._


/** A class for working with an analyzed token
* as part of a syntactic formula.
*
* @param tkn The specific token under consideration.
*/
case class FormulaUnit(tkn: AnalyzedToken)  {

  /** True if tkn is a conjugated verb form.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def verbToken : Boolean = {
    tkn.analyses(0) match {
      case v: VerbForm => true
      case _ => false
    }
  }

  /** True if tkn is a noun.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def nounToken : Boolean = {
    tkn.analyses(0) match {
      case n: NounForm => true
      case _ => false
    }
  }

  /** True if tkn is an adjective.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def adjToken : Boolean = {
    tkn.analyses(0) match {
      case n: AdjectiveForm => true
      case _ => false
    }
  }


  /** List of possible values for grammatical case.  For a
  * substantive (noun, adj, ptcpl), this should be a non-empty
  * Vector of GrammaticalCase values.
  * For other "parts of speech," this will be an empty Vector.
  */
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


  /** List of possible values for gender.  For a
  * substantive (noun, adj, ptcpl), this should be a non-empty
  * Vector of Gender values.
  * For other "parts of speech," this will be an empty Vector.
  */
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

  /** List of possible values for gender.  For a
  * substantive (noun, adj, ptcpl) or a conjugaged verb form,
  * this should be a non-empty
  * Vector of Gender values.
  * For other "parts of speech," this will be an empty Vector.
  */
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


  /** List of Gender/Case/Number triples.  For a
  * substantive (noun, adj, ptcpl), this should be a non-empty
  * Vector of GCNTriples.
  * For other "parts of speech," this will be an empty Vector.
  */
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
