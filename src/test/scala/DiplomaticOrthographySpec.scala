package edu.holycross.shot.ocre
import edu.holycross.shot.mid.validator._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import org.scalatest.FlatSpec




class DiplomaticOrthographySpec extends FlatSpec {


  val legend = "ANTONINVS AVG PIVS P P IMP II"
  val urn = CtsUrn("urn:cts:hcnum:issues.ric.raw:3.ant.868.obv.1")
  val cn = CitableNode(urn, legend)


  "The DiplomaticLegendOrthography object" should "have a label" in {
    val expected = "Diplomatic orthography of OCRE coin legends"
    assert(DiplomaticLegendOrthography.orthography == expected)
  }

  it should "determine if a code point is valid" in {
    val ok = 'A'.toInt
    assert(DiplomaticLegendOrthography.validCP(ok))

    val bad = 'Ä'.toInt
    assert(DiplomaticLegendOrthography.validCP(bad) == false)
  }

  it should "identify accepted token categories"  in {
    val expected = Set(LexicalToken, PunctuationToken, MarkupToken)
    assert (DiplomaticLegendOrthography.tokenCategories.toSet == expected)
  }

  it should "parse a citable node into a sequence of tokens" in {
    val tkns = DiplomaticLegendOrthography.tokenizeNode(cn)
    val expectedCount = 7
    assert(tkns.size == expectedCount)
    val expectedFirst = MidToken(CtsUrn("urn:cts:hcnum:issues.ric.raw_tkns:3.ant.868.obv.1.0"),"ANTONINVS",Some(LexicalToken))

    assert(tkns(0) == expectedFirst)

  }

}
