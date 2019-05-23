package edu.holycross.shot.ocre
import edu.holycross.shot.mid.validator._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.scalatest.FlatSpec




class NormalizedOrthographySpec extends FlatSpec {


  val legend = "antoninvs avgvstvs pivs pater patriae imperator Ⅱ"
  val urn = CtsUrn("urn:cts:hcnum:issues.ric.raw:3.ant.868.obv.1")
  val cn = CitableNode(urn, legend)


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
  it should "parse a citable node into a sequence of tokens" in {
    val tkns = NormalizedLegendOrthography.tokenizeNode(cn)

    val expectedCount = 7
    assert(tkns.size == expectedCount)

    val expectedLexical = 6
    val lexicalTkns = tkns.filter(_.tokenCategory.toString == "Some(LexicalToken)")
    assert(lexicalTkns.size == expectedLexical)

    val expectedNumeric = 1
    val numericTkns = tkns.filter(_.tokenCategory.toString == "Some(NumericToken)")
    assert(numericTkns.size == expectedNumeric)
  }
}
