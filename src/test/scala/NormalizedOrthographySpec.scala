package edu.holycross.shot.ocre
import edu.holycross.shot.mid.validator._
import org.scalatest.FlatSpec




class NormalizedOrthographySpec extends FlatSpec {

  "The NormalizedLegendOrthography object" should "have a label" in {
    val expected = "Orthography for normalized and expanded edition of OCRE coin legends"
    assert(NormalizedLegendOrthography.orthography == expected)
  }

  it should "determine if a code point is valid" in pending
  it should "identify accepted token categories"  in {
    val expected = Set(LexicalToken, PunctuationToken, MarkupToken)
    assert (NormalizedLegendOrthography.tokenCategories.toSet == expected)
  }
  it should "parse a citable node into a sequence of tokens" in pending
}
