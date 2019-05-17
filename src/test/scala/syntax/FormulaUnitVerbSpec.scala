package edu.holycross.shot.ocre.syntax

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.ocre._
import edu.holycross.shot.tabulae._


import org.scalatest.FlatSpec




class FormulaUnitVerbSpec extends FlatSpec {

  val fst = "> fecit\n<u>ocremorph.n17516b</u><u>ls.n17516</u><#>fec<verb><pftact><div><pftact><verb>it<3rd><sg><pft><indic><act><u>livymorph.pftact_pft3</u>\n".split("\n").toVector
  val analyzedTokens = FstFileReader.parseFstLines(fst)
  val tkn = analyzedTokens(0)

  "A FormulaUnit" should "parse an anlyzed verb token into a formula unit" in {
    try {
      val fu : FormulaUnit = FormulaUnit(analyzedTokens(0))
      assert(true)
    } catch {
      case e: Throwable => fail("Could not create FormulaUnit from token " + analyzedTokens(0))
    }
  }

  it should "recognized the category of verb tokens" in {
    val fu : FormulaUnit = FormulaUnit(analyzedTokens(0))
    assert(fu.verbToken)
  }


  it should "recognized the person of a verb token" in {
    val fu : FormulaUnit = FormulaUnit(tkn)
    val personVector = fu.person
    assert(personVector.size == 1)
    assert(personVector(0) == Third)
  }

  it should "recognized the number of a verb token" in {
    val fu : FormulaUnit = FormulaUnit(tkn)
    val numberVector = fu.grammNumber
    assert(numberVector.size == 1)
    assert(numberVector(0) == Singular)
  }

  it should "recognized the tense of a verb token" in {
    val fu : FormulaUnit = FormulaUnit(tkn)
    val tenseVector = fu.tense
    assert(tenseVector.size == 1)
    assert(tenseVector(0) == Perfect)
  }

  it should "recognized the mood of a verb token" in {
    val fu : FormulaUnit = FormulaUnit(tkn)
    val moodVector = fu.mood
    assert(moodVector.size == 1)
    assert(moodVector(0) == Indicative)
  }

  it should "recognized the voice of a verb token" in {
      val fu : FormulaUnit = FormulaUnit(tkn)
      val voiceVector = fu.voice
      assert(voiceVector.size == 1)
      assert(voiceVector(0) == Active)
  }

}
