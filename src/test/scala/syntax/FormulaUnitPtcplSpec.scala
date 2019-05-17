package edu.holycross.shot.ocre.syntax

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.ocre._
import edu.holycross.shot.tabulae._


import org.scalatest.FlatSpec




class FormulaUnitPtcplSpec extends FlatSpec {

  val fst = "> designatvs\n<u>ocremorph.n13382c</u><u>ls.n13382</u><#>designat<verb><pftpass><div><pftpass><ptcpl>vs<masc><nom><sg><pft><pass><u>ocremorph.pft_perfppl1</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)
  val tkn = analyzedTokens(0)

  "A FormulaUnit" should "parse an analyzed particple token into a formula unit" in {
    try {
      val fu : FormulaUnit = FormulaUnit(tkn)
      assert(true)
    } catch {
      case e: Throwable => fail("Could not create FormulaUnit from token " + tkn)
    }
  }

  it should "recognized the category of participle tokens" in {
    val fu : FormulaUnit = FormulaUnit(tkn)
    assert(fu.ptcplToken)
  }


  it should "recognize the gender of participle forms" in {
    val fu : FormulaUnit = FormulaUnit(tkn)
    val genderVector = fu.substGender
    assert(genderVector.size == 1)
    assert(genderVector(0) == Masculine)
  }

  it should "recognize the grammatical case of participle forms" in  {
    val fu : FormulaUnit = FormulaUnit(tkn)
    val caseVector = fu.substCase
    assert(caseVector.size == 1)
    assert(caseVector(0) == Nominative)
  }

  it should "recognize the number of participle forms" in {
    val fu : FormulaUnit = FormulaUnit(tkn)
    val numberVector = fu.grammNumber
    assert(numberVector.size == 1)
    assert(numberVector(0) == Singular)
  }

  it should "construct a GCNTriple for a participle form" in  {
    val fu : FormulaUnit = FormulaUnit(analyzedTokens(0))
    val gcnVector = fu.gcn

    val expected = GCNTriple(Masculine, Nominative, Singular)
    assert(gcnVector(0) == expected)
  }

}
