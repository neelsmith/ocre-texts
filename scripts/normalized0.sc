//
// Read clean data from OCRE:
// apply mappings to *full text* values from mappings1.csv
//


val inputFile = "ocre-data/diplomatic0.cex"
val outputFile = "ocre-data/normalized0.cex"
val fulTextMappingsFile = "mappings/mappings1.csv"

println("\n\nLoading clean diplomatic corpus;")
println("applying conversion of full text contents from file mappings1.csv.")

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

val modifiedNodes = for (pair <- pairs) yield {
  val matchingNodes = corpus.nodes.filter(_.text == pair(0))
  for (n <- matchingNodes) yield {
    CitableNode(n.urn, pair(1))
  }
}

val modifiedCorpus = Corpus(modifiedNodes.flatten)




println("\n\nProfile of resulting corpus:\n")
//OU.profileCorpus(Corpus(cleanTexts))

println("\n\nWriting resulting corpus to " + outputFile )


import java.io.PrintWriter
//new PrintWriter(outputFile){ write(cleanTexts.map( n => n.urn + "#" + n.text).mkString("\n")); close;}
