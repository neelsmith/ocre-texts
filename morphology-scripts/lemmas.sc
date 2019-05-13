import scala.io.Source

val f = "morphology-scripts/ls_indexData.txt"

val lns = Source.fromFile(f).getLines.toVector

def lookup(id: String): Option[String] = {
  val matching = lns.filter(_.startsWith(id + "#"))
  matching.size match {
    case 1 => Some(matching(0))
    case _ => None
  }
}


def lemmaIdForToken(t: String) : String = {
  val parsed = parseObjs.filter(_.token == t)
  parsed(0).analyses(0).lemma.replaceFirst("^[^\\.]+\\.", "")
}

def formatToken(t: MidToken) : String = {
  t.tokenCategory match {
    case Some(LexicalToken) => {
      //
      t.string + " <span style=\"color:gray\">" + lemmaIdForToken(t.string) + "</span>"
    }
    case Some(NumericToken) => { t.string }
    case _ => t.string
  }
}

/*
Things to figure out:

- token index
- no. of occurrences per token
- lemma for each token
- interleaved token/lemma edition


val textList = tkns.map(_.urn.collapsePassageBy(1)).distinct


*/
/*
 def termIndex(term: String, idx: Vector[(String, scala.collection.immutable.Vector[(String, edu.holycross.shot.cite.CtsUrn)])] = tknIdx) : Vector[CtsUrn] ={
     |   val matches = idx.filter(_._1 == term)
     |   matches.size match {
     |     case 1 => {
     |       val solved = matches(0)
     |       solved._2.map(_._2)
     |     }
     |     case 0 => {
     |       println("No matches for " + term)
     |       Vector.empty[CtsUrn]
     |     }
     |     case 2 => {
     |       println(s"Failed: ${matches.size} matches for " + term)
     |       Vector.empty[CtsUrn]
     |     }
     |   }
     | }
     */
/*
val tkns = NormalizedLegendOrthography.tokenizeCorpus(goods)
val lextkns = tkns.filter(_.tokenCategory == Some(LexicalToken))
val tknIdx = lextkns.map( t => (t.string,t.urn)).groupBy(_._1).toVector.sortBy(_._1)
*/


/*

val parseObs = FstFileReader.formsFromFile("output.txt")
val ids = parseObs.map(_.analyses(0).lemma).distinct
ids.map(_.replaceFirst("^[^\\.]+\\.","")).map(id => (id, lookup(id)) )
*/
