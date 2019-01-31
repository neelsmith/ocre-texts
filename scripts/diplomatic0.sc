// Before running, :load scripts/common.sc
//
// Read raw data directly from OCRE:
// Filter out all entries from RIC 10. (Editorial decision)
// Filter out all texts with invalid characters.
//

val inputFile =  "ocre-data/raw.cex"
val outputFile = "ocre-data/diplomatic0.cex"


println("\n\nLoading raw corpus;  converting to pure diplomatic edition containing only fully intelligible legends.")
val corpus = loadCorpus(inputFile)
println("Initial raw corpus: " + corpus.nodes + " legends.\n\n")


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

//new PrintWrtier(outputFile){ write(); close;}
