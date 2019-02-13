import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source

import edu.holycross.shot.mid.validator._

import edu.holycross.shot.ocre._

val src = "ocre-data/normalized7.cex"

val corpus = OcreUtilities.loadCorpus(src)


println("\n\nFinding bad orthography...\n")
val bad = OcreUtilities.badOnly(corpus)

println("\nRemaining uncrorected legends: " + bad.size + "\n")
println("Profile of remaining legends by number of tokens:\n")


println("\nCreating subsets of bad legends by RIC volume...")
val ric1 = Corpus(bad.nodes.filter(_.urn.toString.contains("raw:1_2")))


val ric2_1 = Corpus(bad.nodes.filter(_.urn.toString.contains("raw:2_")))


val ric2 = Corpus(bad.nodes.filter(_.urn.toString.contains("raw:2.")))

println("\n\nSubsets of `bad` corpus by RIC volume:")
println("\nCreated corpus `ric1` (Augustus through the year of four emperors)")
println("Created corpus `ric2_1 (Vespasian, Domitian, Titus)`")
println("Created corpus `ric2` (Nerva, Trajan, Hadrian)\n")


//OcreUtilities.profileTokenLength(bad)


/*
// Example: get subset of legends with 6 tokens :


val sextuples = OcreUtilities.byTokenCount(bad,6)

// and profile how frequently texts appear:

OcreUtilities.profileMessageFreqs(sextuples)


*/
