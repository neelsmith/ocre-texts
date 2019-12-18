import edu.holycross.shot.ocre._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import scala.io.Source


val badCorpusFile = "cex-in-progress/ric-1-3-bad-ortho.cex"
val badCorpus = CorpusSource.fromFile(badCorpusFile)

val finishThese = Vector(
  "1_2.aug", "1_2.tib", "1_2.gai", "1_2.ner",  "1_2.gal", "1_2.ot", "1_2.cl", "1_2.vit",  "1_2.cw", "1_2.clm",  "2_1_2.ves", "2_1_2.tit", "2_1_2.dom"
)

def collectRemainders(badSource : Vector[CitableNode], idFilters : Vector[String], subset: Vector[CitableNode] = Vector.empty[CitableNode]): Vector[CitableNode] = {
  if (idFilters.isEmpty) {
    subset
  } else {
    val currentFilter = idFilters.head
    val newData = badSource.filter(_.urn.passageComponent.startsWith(currentFilter) )
    collectRemainders(badSource, idFilters.tail, subset ++ newData)
  }
}

val tbdNodes = collectRemainders(badCorpus.nodes.distinct, finishThese).distinct
val tbdCorpus = Corpus(tbdNodes)


val groupedLegends = tbdNodes.groupBy(_.urn.collapsePassageTo(2).passageComponent)


val textsGroupedVect = for (auth <- finishThese) yield {
  try {
    val legends = groupedLegends(auth).map(_.text).distinct
    (auth -> legends)
  } catch {
    case t : Throwable => (auth -> Vector.empty[CitableNode])
  }

}
val textsGrouped = textsGroupedVect.toMap


println("\n\nLEGENDS with bad orthography: " + tbdCorpus.size)
println("Clustered by authority: ")
for (auth <- finishThese) {
  try {
    println(auth + ": " + groupedLegends(auth).size)
  } catch {
    case t: Throwable => println(auth  + ": no bad legends!")
  }

}


println("\nDISTINCT TEXTS with bad orthography: " + tbdCorpus.nodes.map(_.text).distinct.size)
println("Clustered by authority: ")
for (auth <- finishThese) {
  try {
    println(auth + ": " + textsGrouped(auth).size)
  } catch {
    case t: Throwable => println(auth  + ": no bad legends!")
  }
}

println("\nAugustus to check;")
println(textsGrouped("1_2.aug").mkString("\n"))
