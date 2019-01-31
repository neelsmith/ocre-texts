// Before running, :load scripts/common.sc
//
// Read raw data directly from OCRE:
// Filter out all entries from RIC 10. (Editorial decision)
// Filter out all texts with invalid characters.
//

val inputFile =  "ocre-data/raw.cex"
val outputFile = "ocre-data/diplomatic0.cex"

println("\n\nLoading raw corpus;  converting to pure diplomatic edition containing only fully intelligible legends.")

import edu.holycross.shot.ocre.{DiplomaticLegendOrthography, OcreUtilities => OU}
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._

val corpus = OU.loadCorpus(inputFile)
println("\n\nInitial raw corpus: " + corpus.nodes.size + " legends.\n\n")


// 1. Eliminate entries from RIC 10.
val minusRIC10 = corpus.nodes.filterNot(_.urn.passageComponent.startsWith("10."))
val expectedTexts = 96699
require(minusRIC10.size == expectedTexts, s"Expected ${expectedTexts} texts, but found ${minusRIC10.size}")
println("\n\nRemoved entries from RIC 10, leaving " + minusRIC10.size + " legends.\n\n")

// 2. Eliminate entries with bad orthography
val cleanTexts = minusRIC10.filter( n => DiplomaticLegendOrthography.validString(n.text))
val expectedClean = 93733
require(cleanTexts.size == expectedClean, s"Expected ${expectedClean} texts, but found ${cleanTexts.size}")
println("\n\nRemoved entries with invalid orthography, leaving " + cleanTexts.size + " legends.\n\n")


println("\n\nProfile of resulting corpus:\n")
OU.profileCorpus(Corpus(cleanTexts))

println("\n\nWriting resulting corpus to " + outputFile )


import java.io.PrintWriter
new PrintWriter(outputFile){ write(cleanTexts.map( n => n.urn + "#" + n.text).mkString("\n")); close;}
