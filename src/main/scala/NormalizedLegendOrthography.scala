package edu.holycross.shot.ocre

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

object NormalizedLegendOrthography extends MidOrthography {


  val alphabet = "abcdefghijklmnopqrstuvwxyz_[]•● ←ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅬⅭⅮⅯ"
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


  /** Parse a citable node into a sequence of MidTokens.
  * Required by MidOrthography trait.
  *
  * @param n CitableNode to tokenize.
  */
  def tokenizeNode(n: CitableNode): Vector[MidToken] = {
    Vector.empty[MidToken]
  }

}
