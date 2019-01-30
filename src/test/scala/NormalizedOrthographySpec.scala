package edu.holycross.shot.ocre
import edu.holycross.shot.mid.validator._
import org.scalatest.FlatSpec




class NormalizedOrthographySpec extends FlatSpec {

  "The NormalizedLegendOrthography object" should "have a label" in {
    val expected = "Orthography for normalized and expanded edition of OCRE coin legends"
    assert(NormalizedLegendOrthography.orthography == expected)
  }

  it should "determine if an alphabetic code point is valid" in {
      val no = 'A'.toInt
      assert(NormalizedLegendOrthography.validCP(no) == false)

      val yes = 'a'.toInt
      assert(NormalizedLegendOrthography.validCP(yes) )
  }
  it should "accept Unicode code points for Roman numerals 1-1000" in {
    val one = 'Ⅰ'.toInt
    assert(NormalizedLegendOrthography.validCP(one))

    val upperI = 'I'.toInt
    assert(NormalizedLegendOrthography.validCP(upperI) == false)
    //←
    //Ⅰ
    //ⅡⅢⅣⅤⅥⅦⅧⅨⅩⅬⅭⅮⅯ
  }

  it should "identify accepted token categories"  in {
    val expected = Set(LexicalToken, PunctuationToken,  NumericToken)
    assert (NormalizedLegendOrthography.tokenCategories.toSet == expected)
  }
  it should "parse a citable node into a sequence of tokens" in pending
}
