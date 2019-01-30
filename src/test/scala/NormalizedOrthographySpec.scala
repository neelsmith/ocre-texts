package edu.holycross.shot.ocre

import org.scalatest.FlatSpec




class NormalizedOrthographySpec extends FlatSpec {

  "The NormalizedLegendOrthography object" should "have a label" in {
    val expected = "Orthography for normalized and expanded edition of OCRE coin legends"
    assert(NormalizedLegendOrthography.orthography == expected)
  }
}
