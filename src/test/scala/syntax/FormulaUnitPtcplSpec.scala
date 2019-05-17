package edu.holycross.shot.ocre.syntax

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.ocre._
import edu.holycross.shot.tabulae._


import org.scalatest.FlatSpec




class FormulaUnitPtcplSpec extends FlatSpec {

  val fst = "> aeternae\n<u>ocremorph.n1413</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><nom><pl><pos><u>ocremorph.us_a_um42</u>\n<u>ocremorph.n1413</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><gen><sg><pos><u>ocremorph.us_a_um38</u>\n<u>ocremorph.n1413</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><dat><sg><pos><u>ocremorph.us_a_um39</u>\n<u>ocremorph.n1413</u><u>ls.n1413</u>aetern<adj><us_a_um><div><us_a_um><adj>ae<fem><voc><pl><pos><u>ocremorph.us_a_um48</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)
  val firstToken = analyzedTokens(0)

  "A FormulaUnit" should "parse an analyzed particple token into a formula unit" in pending/*{
    try {
      val fu : FormulaUnit = FormulaUnit(firstToken)
      assert(true)
    } catch {
      case e: Throwable => fail("Could not create FormulaUnit from token " + firstToken)
    }
  }*/

  it should "recognized the category of participle tokens" in pending/*{
    val fu : FormulaUnit = FormulaUnit(firstToken)
    assert(fu.adjToken)
  }*/


  it should "recognize the gender of participle forms" in pending/*() {
    val fu : FormulaUnit = FormulaUnit(firstToken)
    val genderVector = fu.substGender
    assert(genderVector.size == 1)
    assert(genderVector(0) == Feminine)
  }*/

  it should "recognize the grammatical case of participle forms" in pending/*  {
    val fu : FormulaUnit = FormulaUnit(firstToken)
    val caseVector = fu.substCase
    assert(caseVector.size == 3)
    assert(caseVector(0) == Nominative)
  }*/

  it should "recognize the number of participle forms" in pending/*{
    val fu : FormulaUnit = FormulaUnit(firstToken)
    val numberVector = fu.grammNumber
    assert(numberVector.size == 2)
    assert(numberVector(0) == Plural)
  }*/

  it should "construct a GCNTriple for a participle form" in  pending/*{
    val fu : FormulaUnit = FormulaUnit(analyzedTokens(0))
    val gcnVector = fu.gcn

    val expected = GCNTriple(Feminine, Nominative, Plural)
    assert(gcnVector(0) == expected)
  }
*/
}
