import scala.io.Source

val raw = "ocre-data/raw.cex"
val lines = Source.fromFile(raw).getLines.toVector


val txts = for (l <- lines) yield {
    val cols = l.split("#")
    cols.size match {
      case 2 => cols(1)
      case _ => {
        println("BAD DATA: " + l)
        ""
      }
    }
}


val lens = txts.map( s => (s.size -> s))
