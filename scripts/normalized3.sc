//
// Read clean data from OCRE:
// apply mappings to *full text* values from mappings1.csv
//


val inputFile = "ocre-data/normalized2.cex"
val outputFile = "ocre-data/normalized3.cex"
val fulTextMappingsFile = "mappings/mappings3.csv"

println("\n\nLoading clean diplomatic corpus;")
println("applying conversion of full text contents from file mappings1.csv.\n\n")

import edu.holycross.shot.ocre.{DiplomaticLegendOrthography, OcreUtilities => OU}
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import scala.io.Source

val corpus = OU.loadCorpus(inputFile)
println("\n\nOriginal corpus: " + corpus.size + " legends.")

val mappings = Source.fromFile(fulTextMappingsFile).getLines.toVector
val pairs = for (mapping <- mappings) yield {
  mapping.split(",")
}

println("\n\nRead " + mappings.size + " mappings to normalized text.\n")

val modifiedNodes = for (pair <- pairs) yield {
  val matchingNodes = corpus.nodes.filter(_.text == pair(0))
  for (n <- matchingNodes) yield {
    CitableNode(n.urn, pair(1))
  }
}

val modifiedCorpus = Corpus(modifiedNodes.flatten)

println("\n\nApplied mappings to a total of " + modifiedCorpus.size + " legends.\n")

val merged = OU.merge(corpus,modifiedCorpus)



println("\n\nProfile of resulting corpus:\n")
OU.profileCorpus(merged)

println("\n\nWriting resulting corpus to " + outputFile )


import java.io.PrintWriter
new PrintWriter(outputFile){ write(merged.nodes.map( n => n.urn + "#" + n.text).mkString("\n")); close;}
