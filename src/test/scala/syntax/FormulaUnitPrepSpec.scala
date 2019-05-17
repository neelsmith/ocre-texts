package edu.holycross.shot.ocre.syntax

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.ocre._
import edu.holycross.shot.tabulae._


import org.scalatest.FlatSpec




class FormulaUnitPrepSpec extends FlatSpec {

  val fst = "> ex\n<u>ocremorph.n16519</u><u>ls.n16519</u>ex<indecl><indeclprep><div><indeclprep><indecl><u>indeclinfl.1</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)
  val tkn = analyzedTokens(0)

  "A FormulaUnit" should "parse a preposition token into a formula unit" in {
    try {
      val fu : FormulaUnit = FormulaUnit(tkn)
      assert(true)
    } catch {
      case e: Throwable => fail("Could not create FormulaUnit from token " + tkn)
    }
  }

  it should "recognize the a preposition as an indeclinable form" in {
    val fu : FormulaUnit = FormulaUnit(tkn)
    val indeclVector = fu.indeclPos
    assert(indeclVector.size == 1)
    assert(indeclVector(0) == Preposition)
  }

  it should "recognize the part of speech as a preposition" in {
    val fu : FormulaUnit = FormulaUnit(tkn)
    assert(fu.prepToken)
  }


}
