package edu.holycross.shot.ocre.syntax

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.ocre._
import edu.holycross.shot.tabulae._


import org.scalatest.FlatSpec




class FormulaUnitNounSpec extends FlatSpec {

  val fst = "> adsertori\n<u>ocremorph.n4035</u><u>ls.n4035</u>adsertor<noun><masc><0_is><div><0_is><noun>i<masc><dat><sg><u>ocremorph.0_is3</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)


  "A FormulaUnit" should "parse an anlyzed noun token into a formula unit" in {
    try {
      val fu : FormulaUnit = FormulaUnit(analyzedTokens(0))
      assert(true)
    } catch {
      case e: Throwable => fail("Could not create FormulaUnit from token " + analyzedTokens(0))
    }
  }

  it should "recognized the category of noun tokens" in {
    val fu : FormulaUnit = FormulaUnit(analyzedTokens(0))
    assert(fu.nounToken)
  }

  it should "recognize the grammatical case of nouns" in {
    val fu : FormulaUnit = FormulaUnit(analyzedTokens(0))
    val caseVector = fu.substCase
    assert(caseVector.size == 1)
    assert(caseVector(0) == Dative)
  }
}
