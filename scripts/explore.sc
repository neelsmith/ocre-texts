import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source

import edu.holycross.shot.mid.validator._

import edu.holycross.shot.ocre._

val src = "ocre-data/normalized4.cex"

val corpus = OcreUtilities.loadCorpus(src)


println("\n\nFinding bad orthography...\n")
val bad = OcreUtilities.badOnly(corpus)

println("\nRemaining uncrorected legends: " + bad.size + "\n")
println("Profile of remaining legends by number of tokens:\n")
//OcreUtilities.profileTokenLength(bad)


/*
// Example: get subset of legends with 6 tokens :


val sextuples = OcreUtilities.byTokenCount(bad,6)

// and profile how frequently texts appear:

OcreUtilities.profileMessageFreqs(sextuples)


*/
