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

  /** True if tkn is a particple.
  * This shortcut assumes that while there may
  * be multiple anlayses for a token, they will
  * all belong to the same analytical category ("part of speech").
  */
  def ptcplToken : Boolean = {
    tkn.analyses(0) match {
      case n: ParticipleForm => true
      case _ => false
    }
  }

  //
  // Common to all substantive forms (noun, adj, ptcpl):  GCN
  //
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
            case ptcpl : ParticipleForm => Some(ptcpl.gender)
            case _ => None
        }
      }
      genderList.flatten.toVector.distinct
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
            case ptcpl : ParticipleForm => Some(ptcpl.grammaticalCase)
            case _ => None
        }
      }
      caseList.flatten.toVector.distinct.filterNot(_ == Vocative)
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
            case ptcpl : ParticipleForm => Some(ptcpl.grammaticalNumber)
            case v : VerbForm => Some(v.grammaticalNumber)
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
            case ptcpl : ParticipleForm => Some(GCNTriple(ptcpl.gender, ptcpl.grammaticalCase, ptcpl.grammaticalNumber))
            //case cverb:
            // case participle:
            case _ => None
        }
      }
      tripleList.flatten.toVector.distinct
    }
  }


  //
  // Common to all VERBAL forms (conjugated, inf, ptcpl):  tense, voice
  //

  /** List of possible values for tense.  For a
  * verbal form (conjugated form, infinitive, ptcpl), this should be a non-empty
  * Vector of Tense values.
  * For other "parts of speech," this will be an empty Vector.
  */
  def tense: Vector[Tense] = {
    if (tkn.analyses.isEmpty) {
      Vector.empty[Tense]
    } else {
      val tenseList = for (lysis <- tkn.analyses) yield {
        lysis match {
            case v : VerbForm => Some(v.tense)
            // infinitive
            case ptcpl : ParticipleForm => Some(ptcpl.tense)
            case _ => None
        }
      }
      tenseList.flatten.toVector.distinct
    }
  }

  /** List of possible values for voice.  For a
  * verbal form (conjugated form, infinitive, ptcpl), this should be a non-empty
  * Vector of Voice values.
  * For other "parts of speech," this will be an empty Vector.
  */
  def voice: Vector[Voice] = {
    if (tkn.analyses.isEmpty) {
      Vector.empty[Voice]
    } else {
      val voiceList = for (lysis <- tkn.analyses) yield {
        lysis match {
            case v : VerbForm => Some(v.voice)
            // infinitive
            case ptcpl : ParticipleForm => Some(ptcpl.voice)
            case _ => None
        }
      }
      voiceList.flatten.toVector.distinct
    }
  }

  // Specific to conjugated forms:  Person, Mood.
  def person: Vector[Person] = {
    if (tkn.analyses.isEmpty) {
      Vector.empty[Person]
    } else {
      val personList = for (lysis <- tkn.analyses) yield {
        lysis match {
            case v : VerbForm => Some(v.person)
            case _ => None
        }
      }
      personList.flatten.toVector.distinct
    }
  }


  def mood: Vector[Mood] = {
    if (tkn.analyses.isEmpty) {
      Vector.empty[Mood]
    } else {
      val moodList = for (lysis <- tkn.analyses) yield {
        lysis match {
            case v : VerbForm => Some(v.mood)
            case _ => None
        }
      }
      moodList.flatten.toVector.distinct
    }
  }

}
