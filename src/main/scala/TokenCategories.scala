package edu.holycross.shot.ocre

import edu.holycross.shot.mid.validator._


/** A token for editorial markup.*/
case object MarkupToken extends MidTokenCategory {
  def name = "markup"
}
