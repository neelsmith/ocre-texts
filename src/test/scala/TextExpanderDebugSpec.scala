package edu.holycross.shot.ocre
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import org.scalatest.FlatSpec




class TextExpanderDebugSpec extends FlatSpec {
  "The TextExpander object" should "OK these" in {
    val strs = Vector(
      "senatvs popvlvs+qve romanvs imperator caesar",
      "iovi optimo maximo senatvs popvlvs+qve romanvs vota svscepta pro salvte imperatoris caesaris qvod per", "evm res pvblica in ampliore atqve tranqvilliore statv est",
      "senatvs popvlvs+qve romanvs caesari avgvsto",
      "senatvs popvlvs+qve romanvs imperatori caesari",
      "senatvs popvlvs+qve romanvs imperatori caesari avgvsto consvli ⅩⅠ tribvnicia potestate Ⅵ",
      "senatvs popvlvs+qve romanvs",
      "senatvs popvlvs+qve romanvs parenti conservatori svo",
      "iovi votis svsceptis pro salvte caesaris avgvsti senatvs popvlvs+qve romanvs"
    )
    val cns = for ( (s,i) <-strs.zipWithIndex) yield {
      val u = CtsUrn("urn:cts:dummy:group.version:" + i)
      CitableNode(u, s)
    }
    val c = Corpus(cns)
    println("\n\nEXAMINE TEST GROUP OF " + c.size + " nodes.")
    val ok = TextExpander.validOrtho(c)
    println(ok)

  }




}
