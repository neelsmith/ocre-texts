// 1. Add maven repository where we can find our libraries
val myBT = coursierapi.MavenRepository.of("https://dl.bintray.com/neelsmith/maven")
interp.repositories() ++= Seq(myBT)

// 2. Make libraries available with `$ivy` imports:
import $ivy.`edu.holycross.shot::ohco2:10.16.0`
import $ivy.`edu.holycross.shot.cite::xcite:4.1.1`
import $ivy.`edu.holycross.shot::midvalidator:9.1.0`
import $ivy.`edu.holycross.shot::latphone:2.7.2`
import $ivy.`edu.holycross.shot::latincorpus:2.2.1`
import $ivy.`org.plotly-scala::plotly-almond:0.7.1`
import $ivy.`edu.holycross.shot::nomisma:3.0.0`



// Import everything we'll use
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.latin._
import edu.holycross.shot.latincorpus._

import edu.holycross.shot.nomisma._

import plotly._, plotly.element._, plotly.layout._, plotly.Almond._
repl.pprinter() = repl.pprinter().copy(defaultHeight = 3)

import scala.io.Source

// Read separately computed morphological data from URL:
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

// Combine citable corpus with morphological data to create
// a corpus of parsed tokens:
val ocreTokens = LatinCorpus.fromFstLines(corpus, Latin24Alphabet, fstLines, strict = false)

// Collect occurences for a given token's lexemes:
val token = "libertas"
val lexemeUrns = ocreTokens.tokenLexemeIndex(token)
// here, we assume there's only one matching lexeme:
val lexemeUrn = lexemeUrns(0)
val occurrences =  ocreTokens.lexemeConcordance(lexemeUrn)


// group text passages by issuing authority, by using the
// first two pieces of URN's passage component:
val byAuth = occurrences.groupBy( _.collapsePassageTo(2))
import edu.holycross.shot.histoutils._
val libFreqs = for (auth <- byAuth.keySet) yield {
  val parts = auth.passageComponent.split("\\.")
  println(parts(1) + ": " + byAuth(auth).size + " issues")
  Frequency(parts(1), byAuth(auth).size)
}

val libHist = edu.holycross.shot.histoutils.Histogram(libFreqs.toVector)
val libAuths = libHist.frequencies.map(_.item)
val libCounts = libHist.frequencies.map(_.count)

val libIssuesPlot = Seq(
  Bar(x = libAuths, y = libCounts)
)

plot(libIssuesPlot)



// Assemble normalized/expanded text for each passage:
val psgUrns = occurrences.map(_.collapsePassageBy(1).addVersion("expanded"))
println("Examining " + occurrences.size + " legends.")
val txts = for ((psg,idx) <- psgUrns.zipWithIndex) yield {
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

val colorMap : Map[String, Color.RGB] = Map(
"libertas avgvsta" -> Color.RGB(0,250,0),
"libertas avgvsti" -> Color.RGB(0,0, 250),
"libertas avgvsti senatvs consvlto" -> Color.RGB(100,100, 250),
"libertas pvblica" -> Color.RGB(200,100,0),
"libertas pvblica senatvs consvlto" -> Color.RGB(255,100,100),
"libertas restitvta senatvs consvlto" -> Color.RGB(255,0,0)
)

val expandedCorpus = Corpus(psgUrns.zip(txts).map{ case (u,t) => CitableNode(u,t) })
expandedCorpus.size

// Load ocre:
val ocreCex = "https://raw.githubusercontent.com/neelsmith/nomisma/master/cex/ocre-cite-ids.cex"
val ocre = OcreSource.fromUrl(ocreCex)

def coinForText(ocre: Ocre, legend: CtsUrn): Option[NomismaIssue] = {
  ocre.issue(legend.collapsePassageBy(1).passageComponent)
}

// Join passages and coins
val expandedWithCoins = for ((psg,idx) <- psgUrns.zipWithIndex) yield {
    val coin = coinForText(ocre, psg).get
    //(coin, psg.passageComponent, txts(idx))
    (coin,txts(idx))
}


println(expandedWithCoins(0))
val groupedByText = expandedWithCoins.groupBy(_._2)

def toOcre(srcIssues: Vector[NomismaIssue], outputIssues: Vector[OcreIssue] = Vector.empty[OcreIssue])  :  Vector[OcreIssue]= {

  if (srcIssues.isEmpty) {
    outputIssues
  } else {
    srcIssues.head match {
      case o: OcreIssue => {

        val newOutput = outputIssues ++ Vector(o)
        //println("Add " + o + " to output")
        //pritnln("New output size " + )
        toOcre(srcIssues.tail, newOutput)
      }
      case _ => {
        println("BAD DATA: not an OcreIssue: " + srcIssues.head)
        toOcre(srcIssues.tail, outputIssues)
      }
    }
  }
}


def traceForGroup(issues: ) = {

  val smallOcre = Ocre(issues)
  /*
  Scatter(
    datedTextHisto.frequencies.map(_.item),
    datedTextHisto.frequencies.map(_.count),
    text = datedTextHisto.frequencies.map(_.toString),
    mode = ScatterMode(ScatterMode.Markers),
    marker = Marker(
      size = 8,
      color = Color.RGB(250,100,100)
    )
  )*/
}


val textKeys = groupedByText.keySet.toVector
val miniOcres = for (keyVal <- textKeys) yield {
  val dataSet = groupedByText(keyVal).map(_._1)
  val ocre = toOcre(dataSet)
  //println(keyVal + ", " + dataSet.size)
  (keyVal -> Ocre(ocre))
}



val datedTextGroups = miniOcres.map { case(legend, miniOcre) => {
  (legend, miniOcre.issues.map(issue => (issue.dateRange.get.pointAverage, legend)).groupBy(_._1).map{ case (k,v) => (k, v.size)})
}}


val mapped = datedTextGroups.toMap //("libertas avgvsti"))

mapped("libertas avgvsti").toVector.map(_._1)
val traces = for (legend <- mapped.keySet) yield {
  //println(legend + ", " + colorMap(legend))
  Scatter(
    //datedTextHisto.frequencies.map(_.item),
    mapped(legend).toVector.map(_._1),
    mapped(legend).toVector.map(_._2),
    text = mapped(legend).toString,
    name = legend,
    //datedTextHisto.frequencies.map(_.count),
    //text = datedTextHisto.frequencies.map(_.toString),
    mode = ScatterMode(ScatterMode.Markers),
    marker = Marker(
      size = 8,
      color = colorMap(legend)
    )
  )
}
val tracesData = traces.toSeq

val legendsLayout = Layout(
  title = "Legends with 'libertas'",
  showlegend = true,
  yaxis = Axis(title = "Number of issues"),
  xaxis = Axis(title = "Year CE"),
  height = 600,
  width = 900
)


plot(tracesData, legendsLayout)

//////////////////////
val datedTexts = expandedWithCoins.map{ case (coin, legend) => (coin.dateRange.get.pointAverage, legend)}
// Plot bubble texts by year:
val datedGroups = datedTexts.groupBy(_._1) //.toVector.sortBy(_._1)

println(datedGroups(68).mkString("\n"))



val yrFreqs = for (yr <- datedGroups.keySet) yield {
  Frequency(yr, datedGroups(yr).size)
}
val datedTextHisto = edu.holycross.shot.histoutils.Histogram(yrFreqs.toVector).sorted

//datedTextHisto.total
//println(datedTextHisto.sorted.frequencies.mkString("\n"))
//datedTextHisto.frequencies
val txtTrace = Scatter(
  datedTextHisto.frequencies.map(_.item),
  datedTextHisto.frequencies.map(_.count),
  text = datedTextHisto.frequencies.map(_.toString),
  mode = ScatterMode(ScatterMode.Markers),
  marker = Marker(
    size = 8,
    color = Color.RGB(250,100,100)
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





///
