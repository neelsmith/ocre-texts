package edu.holycross.shot.ocre
import edu.holycross.shot.mid.validator._
import org.scalatest.FlatSpec




class DiplomaticOrthographySpec extends FlatSpec {

  "The DiplomaticLegendOrthography object" should "have a label" in {
    val expected = "Diplomatic orthography of OCRE coin legends"
    assert(DiplomaticLegendOrthography.orthography == expected)
  }

  it should "determine if a code point is valid" in pending
  it should "identify accepted token categories"  in {
    val expected = Set(LexicalToken, PunctuationToken, MarkupToken)
    assert (DiplomaticLegendOrthography.tokenCategories.toSet == expected)
  }
  it should "parse a citable node into a sequence of tokens" in pending

}
