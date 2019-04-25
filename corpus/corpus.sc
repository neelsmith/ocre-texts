import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source
import edu.holycross.shot.mid.validator._
import edu.holycross.shot.ocre._


// expect this script to be loaded from root directory of repo:
val currentSource = "ocre-data/normalized13.cex"

val corpus = OcreUtilities.loadCorpus(currentSource)
println("\n\nFinding corpus of orthographically valid texts...\n")
val goods = OcreUtilities.goodOnly(corpus)
