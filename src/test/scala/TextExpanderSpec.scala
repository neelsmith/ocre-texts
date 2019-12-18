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

  it should "read data from a set of files following convention for sequential file naming" in {
    val root = "src/test/resources/mappings"
    val lastN = 1
    val mappings = TextExpander.loadNMappings(lastN, baseDirectory = root)
    assert(mappings.size == 9)
  }

  it should "define a default corpus" in {
    val defaultCorpus = TextExpander.defaultCorpus
    assert(defaultCorpus.size > 20000)
  }

  it should "expand text from a given corpus given a set of mappings" in {
    val root = "src/test/resources/mappings"
    val mappings = TextExpander.loadNMappings(1, baseDirectory = root)
    val expanded = TextExpander.expandText(TextExpander.defaultCorpus, mappings)
    println("Default corpus has " + TextExpander.defaultCorpus.size)
    println("Exapnded corpus has  " + expanded.size)
  }

  it should "expand a text from a given corpus and index of mappings" in {
    val root = "src/test/resources/mappings"
    val expanded = TextExpander.expandFromMappingsDir(1, root)
    println("Expanded corpus of size " + expanded.size)
  }

  it should "assess the orthographic status of every node  a corpus" in {
    val root = "src/test/resources/mappings"
    val expanded = TextExpander.expandFromMappingsDir(1, root)
    val validExpanded = TextExpander.validOrtho(expanded, true)
    println(validExpanded.size + " valid expanded nodes.")
    val validDiplomatic = TextExpander.validOrtho(expanded, false)
    println(validDiplomatic.size + " valid diplomatic nodes.")
    println("TOTAL: " + (validExpanded.size + validDiplomatic.size))
  }

  it should "simplify finding bad nodes in a source corpus" in {
    val normalized = false
    val validDiplomatic = TextExpander.validOrtho(TextExpander.defaultCorpus, normalized)
    println("Out of " + TextExpander.defaultCorpus.size + " source nodes, " + validDiplomatic.size + " are in valid orthography.")
    val invalidNodes = TextExpander.invalidOrtho(TextExpander.defaultCorpus, normalized)
    println("\n" + invalidNodes.mkString("\n\n"))
  }


}
