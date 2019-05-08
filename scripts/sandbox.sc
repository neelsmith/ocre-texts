import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source

import edu.holycross.shot.mid.validator._

import edu.holycross.shot.ocre._

val c = OcreUtilities.loadCorpus("ocre-data/diplomatic0.cex")
val tkns = NormalizedLegendOrthography.tokenizeCorpus(c)

def ngramming(c: Corpus) = {
   val tknVects = for (n <- c.nodes) yield { NormalizedLegendOrthography.tokenizeNode(n) }
   val sizes = tknVects.map( v => v.size)
   val grouped = sizes.groupBy( i => i)
  grouped.toSeq.map{ case (k,v) => (k, v.size) }
}

val tokenSizeDists = ngramming(c)

val hdr = "Length in tokens,Number texts\n"
println(hdr + tokenSizeDists.sortBy(_._2).reverse.mkString("\n"))







// byTokenCount
