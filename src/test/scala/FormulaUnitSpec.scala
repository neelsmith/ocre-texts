package edu.holycross.shot.ocre
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import org.scalatest.FlatSpec




class FormulaUnitSpec extends FlatSpec {
  "The FormulaUnit object" should "parse an FST file into analyzed tokens" in {
    val fileName = "src/test/resources/tiny1.fst"
    val parsed = FormulaUnit.parses(fileName)
    val expectedParses = 4
    assert(parsed.size == expectedParses)
  }

  it should "do more" in {
    val u = CtsUrn("urn:cts:hcnum:issues.ric.raw:2.hdn.986_as.obv.7")
    val s = "hadrianvs avgvstvs consvl Ⅲ pater patriae"
    val cn = CitableNode(u,s)

    val fst = "src/test/resources/node1.fst"
    val parsed = FormulaUnit.parses(fst)
    val tkns = FormulaUnit.tokens(cn, parsed)
    println(s + " == " + tkns.size + " tokens")
  }

}
