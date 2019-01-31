package edu.holycross.shot.ocre

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


object DiplomaticLegendOrthography extends MidOrthography {

  // FOUND ALL OF THESE IN THE RAW FILE!
  //
   //()*+,.
     /////3:;?ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]_abcdefghilmnoprstuvwx|ÄƮ˙̄̅ΑΓΔΚΛΠΣθϕϟϤϵ—•…←∈●☧Ꜹ"
  /** Ordered string of allowed characters.*/
  val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_[]•● ←"


  /** Label for this orthographic system.
  * Required by MidOrthography trait.
  */
  def orthography = "Diplomatic orthography of OCRE coin legends"


  /** True if `cp` is a valid code point in this orthography.
  * Required by MidOrthography trait.
  */
  def validCP(cp: Int): Boolean = {
    alphabet.contains(cp.toChar)
   }

  /** Set of recognized token categories.
  * Required by MidOrthography trait.
  */
  def tokenCategories = {
    Vector(LexicalToken, PunctuationToken, MarkupToken)
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
    val classified = for (unit <- units.zipWithIndex) yield {
      val newPassage = urn.passageComponent + "." + unit._2
      val newVersion = urn.addVersion(urn.versionOption.getOrElse("") + "_tkns")
      val newUrn = CtsUrn(newVersion.dropPassage.toString + newPassage)

      val trimmed = unit._1.trim
      val tkn = MidToken(newUrn, trimmed, Some(LexicalToken))
      tkn
    }
    classified.toVector
  }

}
