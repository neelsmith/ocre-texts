// 1. Add maven repository where we can find our libraries
val myBT = coursierapi.MavenRepository.of("https://dl.bintray.com/neelsmith/maven")
interp.repositories() ++= Seq(myBT)

// 2. Make libraries available with `$ivy` imports:
import $ivy.`edu.holycross.shot::ohco2:10.16.0`
import $ivy.`edu.holycross.shot.cite::xcite:4.1.1`
import $ivy.`edu.holycross.shot::midvalidator:9.1.0`
import $ivy.`edu.holycross.shot::latphone:2.7.2`
import $ivy.`edu.holycross.shot::latincorpus:2.2.1`


import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.latin._
import edu.holycross.shot.latincorpus._

import scala.io.Source

val fstUrl = "https://raw.githubusercontent.com/neelsmith/hctexts/master/workfiles/ocre/ocre-fst.txt"
val fstLines = Source.fromURL(fstUrl).getLines.toVector

// Read CEX data from URL, create a corpus of citable nodes.
// The `CorpusSource` object should really have a function that does this for you,
// analogous to its `fromFile` function.
val url = "https://raw.githubusercontent.com/neelsmith/hctexts/master/cex/ocre43k.cex"
val ctsLines = Source.fromURL(url).getLines.toVector.tail.filter(_.nonEmpty)

val stringPairs = ctsLines.map(_.split("#"))
val citableNodes = stringPairs.map( arr => CitableNode(CtsUrn(arr(0)), arr(1)))
val corpus = Corpus(citableNodes)

// A corpus of parsed tokens:
val ocrelatin = LatinCorpus.fromFstLines(corpus, Latin24Alphabet, fstLines, strict = false)

val token = "libertas"
val lexemeUrns = ocrelatin.tokenLexemeIndex(token)
// here, we assume there's only one matching lexeme:
val lexemeUrn = lexemeUrns(0)
val occurrences =  ocrelatin.lexemeConcordance(lexemeUrn)



val byAuth = occurrences.groupBy( _.collapsePassageTo(2))


import edu.holycross.shot.histoutils._
val libFreqs = for (auth <- byAuth.keySet) yield {
  val parts = auth.passageComponent.split("\\.")
  println(parts(1) + ": " + byAuth(auth).size + " issues")
  Frequency(parts(1), byAuth(auth).size)
}


import $ivy.`org.plotly-scala::plotly-almond:0.7.1`
import plotly._, plotly.element._, plotly.layout._, plotly.Almond._
repl.pprinter() = repl.pprinter().copy(defaultHeight = 3)

val libHist = edu.holycross.shot.histoutils.Histogram(libFreqs.toVector)
val libAuths = libHist.frequencies.map(_.item)
val libCounts = libHist.frequencies.map(_.count)

val libIssuesPlot = Seq(
  Bar(x = libAuths, y = libCounts)
)

plot(libIssuesPlot)

val psgs = occurrences.map(_.collapsePassageBy(1).addVersion("raw"))



println("Examining " + occurrences.size + " legends.")
val txts = for ((psg,idx) <- psgs.zipWithIndex) yield {
  val matchPassage = corpus.nodes.filter(_.urn ~~ psg)
  print(idx + 1 + ". ")
  matchPassage.distinct.size match {
    case 0 => {
      println("NO MATCH for " + psg.passageComponent)
      ""
    }
    case 1 => {
      println(psg.passageComponent + ": " + matchPassage(0).text)
      matchPassage(0).text
    }
    case _ => {
      println("Found " + matchPassage.distinct.size + " distinct matches  for " + psg.passageComponent)
      matchPassage(1).text
    }
  }
}
println(txts.distinct.sorted.mkString("\n"))




import $ivy.`edu.holycross.shot::nomisma:3.0.0`
import edu.holycross.shot.nomisma._
val ocreCex = "https://raw.githubusercontent.com/neelsmith/nomisma/master/cex/ocre-cite-ids.cex"
val ocre = OcreSource.fromUrl(ocreCex)

def coinForText(ocre: Ocre, legend: CtsUrn): Option[NomismaIssue] = {
  ocre.issue(legend.collapsePassageBy(1).passageComponent)
}

val psgsWithIds = for ((psg,idx) <- psgs.zipWithIndex) yield {
    val coin = coinForText(ocre, psg).get
    (coin, psg.passageComponent, txts(idx))
}

psgsWithIds(0)

val datedTexts = psgsWithIds.map{ case (coin, ref, legend) => (coin.dateRange.get.pointAverage, legend)}

// Plot bubble texts



val datedGroups = datedTexts.groupBy(_._1).toVector.sortBy(_._1)
println(datedGroups.mkString("\n"))

val txtTrace = Scatter(
  datedGroups.map(_._1),
  datedTexts.map(_._2.size),
  text = datedGroups.map(_._1.toString),
  mode = ScatterMode(ScatterMode.Markers),
  marker = Marker(
    //color = Seq(Color.RGB(93, 164, 214), Color.RGB(255, 144, 14), Color.RGB(44, 160, 101), Color.RGB(255, 65, 54)),
    size = 8 //datedTexts.map(_._2.size)
  )
)
val txtData = Seq(txtTrace)
txtData
val txtLayout = Layout(
  title = "Libertas",
  showlegend = false,
  height = 400,
  width = 600
)
plot(txtData,txtLayout)

  val trace1 = Scatter(
    Seq(1, 2, 3, 4),
    Seq(10, 11, 12, 13),
    text = Seq("""A
    size = 40""", """B
    size = 60""", """C
    size = 80""", """D
    size = 100"""),
    mode = ScatterMode(ScatterMode.Markers),
    marker = Marker(
      color = Seq(Color.RGB(93, 164, 214), Color.RGB(255, 144, 14), Color.RGB(44, 160, 101), Color.RGB(255, 65, 54)),
      size = Seq(40, 60, 80, 100)
    )
  )

  val data = Seq(trace1)

  val layout = Layout(
    title = "Bubble Chart Hover Text",
    showlegend = false,
    height = 400,
    width = 600
  )
///

plot(data,layout)
