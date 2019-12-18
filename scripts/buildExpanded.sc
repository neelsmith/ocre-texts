import edu.holycross.shot.ocre._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._




val expanded = TextExpander.expandFromMappingsDir(21, "mappings")


val normalized = true
val validNormalized = TextExpander.validOrtho(expanded, normalized)
println("Out of " + expanded.size + " source nodes, " + validNormalized.size + " are expanded to valid normalized orthography.")
val invalidNodes = TextExpander.invalidOrtho(expanded, normalized)
println("Nodes remain: " + invalidNodes.size)


val problems = invalidNodes.map(_._1)
val probsGrouped = problems.groupBy( cn => cn.urn.collapsePassageTo(2).passageComponent)

val probsCounts = probsGrouped.toVector.map{ case (k,v) => (k,v.size) }

val finishThese = Vector(
  "1_2.tib", "1_2.aug", "1_2.ot", "1_2.cl", "2_1_2.dom", "2_1_2.tit", "2.ner", "1_2.gal", "1_2.ner", "1_2.vit", "1_2.cw", "2_1_2.ves", "1_2.gai", "1_2.clm"
)

def collectRemainders(badSource : Vector[CitableNode], idFilters = Vector[String], subset: Vector[CitableNode] = Vector.empty[CitableNode]): Vector[CitableNode] = {
  if (idFilters.isEmpty) {
    subset
  } else {
    val currentFilter = idFilters.head
    val newData = badSource.filter(_.urn.passageComponent.startsWith(currentFilter) )
    collectRemainders(badSource, idFilters.tail, subset ++ newData)
  }
}
