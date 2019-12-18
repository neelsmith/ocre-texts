package edu.holycross.shot.ocre
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import org.scalatest.FlatSpec




class TextExpanderSpec extends FlatSpec {
  "The TextExpander object" should "load a Vector of mapping strings from a file" in {
    val f = "src/test/resources/mappings/mappings0.csv"
    val mappings = TextExpander.loadMappingFile(f)
    val mappingLines = mappings.split("\n")
    assert(mappingLines.size == 1)

    val f2 = "src/test/resources/mappings/mappings1.csv"
    val mappings2 = TextExpander.loadMappingFile(f2)
    val mappingLines2 = mappings2.split("\n")
    assert(mappingLines2.size == 8)

  }

  it should "load a Vector of mapping strings from a Vector of file na es" in {
    val files = Vector(
      "src/test/resources/mappings/mappings0.csv",
      "src/test/resources/mappings/mappings1.csv")
    val mappings = TextExpander.loadMappings(files)
    assert(mappings.size == 9)
  }


}
