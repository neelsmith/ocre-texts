
import edu.holycross.shot.tabulae._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source
import edu.holycross.shot.mid.validator._
import edu.holycross.shot.ocre._
import java.io.PrintWriter
import sys.process._
import scala.language.postfixOps

//////// CONFIGURE LOCAL SET UP  /////////////////////////////

// CEX file for corpus of texts: relative reference since we
// expect this script to be loaded from root directory of repo
val currentSource = "ocre-data/normalized15.cex"
val parser = "parser/latin.a"

// Explicit paths to SFTS binaries and make.  Adjust SFST paths
// to /usr/bin if using default install on Linux.
val compiler = "/usr/local/bin/fst-compiler-utf8"
val fstinfl = "/usr/local/bin/fst-infl"
val make = "/usr/bin/make"

def msg(txt: String): Unit  = {
  println("\n\n")
  println(txt)
  println("\n")
}

//////// PROCESS NUMISMATIC CORPUS  //////////////////////////
// Intermediate files used in parsing:
val wordsFile = "words.txt"
val parseOutput = "output.txt"
val lewisShort = "ls-data.cex"


val corpus = OcreUtilities.loadCorpus(currentSource)
msg("Creating corpus composed of orthographically valid texts..")
val goodCorpus = OcreUtilities.goodOnly(corpus)
println("Done.")
msg("Tokenizing valid texts..")
val allTokens = NormalizedLegendOrthography.tokenizeCorpus(goodCorpus)
msg("Done.")
val lexTokens = allTokens.filter(_.tokenCategory == Some(LexicalToken))
val forms = lexTokens.map(_.string).distinct.sorted
new PrintWriter(wordsFile){ write(forms.mkString("\n") + "\n"); close; }


val cmd = s"${fstinfl} ${parser} ${wordsFile}"
msg("Beginning to parse word list in " + wordsFile)
println("Please be patient: there will be a pause after")
println("the messages 'reading transducer...' and 'finished' while the parsing takes place.")
val parses = cmd !!
new PrintWriter(parseOutput) {write(parses); close;}
msg("Done.")

msg("Reading SFST output into object model..")
val parsedObjs = FstFileReader.formsFromFile(parseOutput)
msg("Done.")

// Short cut: have previously verified that all forms parse to only one lemma,
// even if they parse to multiple forms of that lemma.
msg("Index forms to lexical entity...")
val formLemmaIndex = parsedObjs.map(parse => (parse.analyses(0).lemmaId, parse.token) )
msg("Done.")


msg("Loading Lewis-Short data for labelling lexical entities")
val ls = Source.fromFile(lewisShort).getLines.toVector
val entries = for (entry <- ls.tail) yield {
  val cols = entry.split("#")
  Cite2Urn(cols(1)).objectComponent + "#" + cols(3)
}
msg("Done.")


def labelLexItem(id: String) : String = {
  val matches = entries.filter(_.startsWith(id + "#"))
  matches.size match {
    case 1 => matches(0)
    case 0 => {println("No matches for " + id); ""}
    case _ => {println("Multiple matches for " + id + ": " + matches.mkString(", ")); ""}
  }
}


/** Retrieve full passage node for a token.*/
def psgForTokenId(u: CtsUrn) : Option[CitableNode] = {
  val psgCorpus = corpus ~~ u.collapsePassageBy(1).dropVersion
  psgCorpus.size match {
    case 1 => Some(psgCorpus.nodes(0))
    case 0 => {println("No passages matching " + u); None}
    case _ => {println("Multiple passages matching " + u + " : " + psgCorpus.nodes.map(_.urn).mkString(", ")); None}
  }
}



def entityForForm(str: String) : String = {
  val matches = formLemmaIndex.filter(_._2 == str).map(_._1)
  matches.size match {
    case 1 => matches(0)
    case 0 =>{println("No matches for " + str); ""}
    case _ =>{println("Multiple matches for " + str + ":  " + matches.mkString(", ")); ""}
  }
}


/** Given an id, find attested surface forms.
* Note that id should be in the abbreviated URN
* format COLLECTION.ID (e.g., "ls.4509").
*/
def formsForEntity(id: String)  = {
  formLemmaIndex.filter(_._1 == id).map(_._2)
}

/** Given a surface form, find URNs for occurrences.*/
def urnsForForm(str: String) : Vector[CtsUrn]= {
  lexTokens.filter(_.string == str).map(_.urn)
}

/** Given a an ID for a lexical entity, find URNs for occurrences.
* Note that id should be in the abbreviated URN
* format COLLECTION.ID (e.g., "ls.4509").
*/
def urnsForEntity(id: String) : Vector[CtsUrn]= {
  val forms = formsForEntity(id)
  val urns = for (form <- forms) yield {
    urnsForForm(form)
  }
  urns.flatten
}


def info: Unit = {
  println("\n\nThings you can do with these data sets:")

  println("\nFind entity ID for a given form:\n")
  println("\tentityForForm(STRING)\n")


  println("\nFind forms for a given entity:\n")
  println("\tformsForEntity(IDVALUE)\n")



  println("\nFind passage URNs for a given form:\n")
  println("\turnsForForm(STRING)\n")


  println("\nFind passage URNs for a given entity:\n")
  println("\turnsForEntity(ID)\n")
}
