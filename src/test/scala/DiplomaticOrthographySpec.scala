package edu.holycross.shot.ocre

import org.scalatest.FlatSpec




class DiplomaticOrthographySpec extends FlatSpec {

  "The DiplomaticLegendOrthography object" should "have a label" in {
    val expected = "Diplomatic orthography of OCRE coin legends"
    assert(DiplomaticLegendOrthography.orthography == expected)
  }
}
