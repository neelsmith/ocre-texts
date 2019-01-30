package edu.holycross.shot.ocre

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

object NormalizedLegendOrthography extends MidOrthography {

  def orthography = "Normalized and expanded orthography of OCRE coin legends"
  def validCP(cp: Int): Boolean = { false }
  def tokenCategories = {
    Vector(LexicalToken, PunctuationToken, MarkupToken)
  }
  def tokenizeNode(n: CitableNode): Vector[MidToken] = {
    Vector.empty[MidToken]
  }

}
