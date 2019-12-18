package edu.holycross.shot.ocre

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

object NormalizedLegendOrthography extends MidOrthography {


  val alphabetic = "abcdefghiklmnopqrstvwxyz"
  val numeric = "ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅬⅭⅮⅯ"
  val punctOrEditorial = "_[]•● ←+"
  val alphabet = alphabetic + numeric + punctOrEditorial
  /** Label for this orthographic system.
  * Required by MidOrthography trait.
  */
  def orthography = "Orthography for normalized and expanded edition of OCRE coin legends"


  /** True if `cp` is a valid code point in this orthography.
  * Required by MidOrthography trait.
  */
  def validCP(cp: Int): Boolean = {   alphabet.contains(cp.toChar) }

  /** Set of recognized token categories.
  * Required by MidOrthography trait.
  */
  def tokenCategories = {
    Vector(LexicalToken, PunctuationToken, NumericToken)
  }


  def isAlphabetic(s: String): Boolean = {
    val tfValues = for (c <- s) yield {
      alphabetic.contains(c) || punctOrEditorial.contains(c)
    }
    val tf = tfValues.distinct
    (tf.size == 1) && (tf(0) == true)
  }
  def isNumeric(s: String): Boolean = {
    val tfValues = for (c <- s) yield {
      numeric.contains(c)
    }
    val tf = tfValues.distinct
    (tf.size == 1) && (tf(0) == true)
  }
  /** Parse a citable node into a sequence of MidTokens.
  * Required by MidOrthography trait.
  *
  * @param n CitableNode to tokenize.
  */
  def tokenizeNode(n: CitableNode): Vector[MidToken] = {
    val urn = n.urn

    // initial chunking on white space
    val units = n.text.split(" ").filter(_.nonEmpty)
    val classified = for (unit <- units) yield {
      val subUnits = unit.split("\\+").toVector

      subUnits.map (
        subUnit => {
          val trimmed = subUnit.trim
          if (isAlphabetic(trimmed)) {
            MidToken(urn, trimmed, Some(LexicalToken))
          } else if (isNumeric(trimmed)) {
            MidToken(urn, trimmed, Some(NumericToken))
          } else {
            MidToken(urn, trimmed, None)
          }
        })
      }
    val allTokens = classified.toVector.flatten
    for ((token,count) <- allTokens.zipWithIndex) yield {
      val newUrn = CtsUrn(token.urn.toString + "." + count)
      MidToken(newUrn, token.string, token.tokenCategory)
    }
  }

}
