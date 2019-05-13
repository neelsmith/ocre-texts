
import edu.holycross.shot.tabulae.builder._
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import sys.process._
import scala.language.postfixOps


val compiler = "/usr/local/bin/fst-compiler-utf8"
val fstinfl = "/usr/local/bin/fst-infl"
val make = "/usr/bin/make"



def compile(repo: String =  "../tabulae") = {
  val tabulae = File(repo)
  val datasets = "."
  val c = "ocre-morphology"
  val conf =  Configuration(compiler,fstinfl,make,datasets)

  try {
    FstCompiler.compile(File(datasets), File(repo), c, conf, true)
    val tabulaeParser = repo/"parsers/ocre-morphology/latin.a"
    val localParser = File("parser/latin.a")
    cp(tabulaeParser, localParser)
    println("\nCompilation completed.  Parser latin.a is " +
    "available in directory \"parser\"\n\n")
  } catch {
    case t: Throwable => println("Error trying to compile:\n" + t.toString)
  }

}


/**  Parse words listed in a file, and return their analyses
* as a String.
*
* @param wordsFile File with words to parse listed one per line.
* @param parser Name of corpus-specific parser, a subdirectory of
* tabulae/parsers.
*/
def parse(wordsFile : String = "words.txt") : String = {
  val cmd = fstinfl + " parser/latin.a  " + wordsFile
  println("Beginning to parse word list in " + wordsFile)
  println("Please be patient: there will be a pause after")
  println("the messages 'reading transducer...' and 'finished' while the parsing takes place.")
  cmd !!
}

def reparse(wordsFile : String = "words.txt") : String = {
  compile()
  parse(wordsFile)
}
def failures (wordsFile : String = "words.txt") : Vector[String]= {
  parse(wordsFile).split("\n").toVector.filter(_.contains("no result")).map(_.replaceAll("no result for ", ""))

}


import java.io.PrintWriter
def writeParse(wordsFile: String = "words.txt", output: String = "output.txt") : Unit = {
  val parses = parse(wordsFile)
  new PrintWriter(output) {write(parses); close;}
}

def writeFailures(wordsFile: String = "words.txt", output: String = "failed.txt") : Unit = {
  val bad = failures(wordsFile)
  new PrintWriter(output) {write(bad.sorted.mkString("\n")); close;}
  println("List of words that failed to parse available in " + output)
}

// quick kludge instead of tokenizing properly...
val numberStrings = Vector("Ⅱ",
  "Ⅲ",
  "Ⅳ",
  "Ⅴ",
  "Ⅷ",
  "Ⅹ",
  "ⅩⅡ",
  "ⅩⅤ",
  "ⅩⅩⅡ",
  "ⅩⅩⅦ",
  "ⅩⅩⅧ"
)

// alphabetized list of unique whitespace-delimited tokens
def wordList(c: edu.holycross.shot.ohco2.Corpus) : Vector[String] = {
  val txts = c.nodes.map(_.text)
  txts.map(_.split(" ")).toVector.flatten.distinct.filterNot(numberStrings.contains(_)).sorted
}

def writeWordList(c: edu.holycross.shot.ohco2.Corpus, output: String = "words.txt") : Unit = {
  val words = wordList(c)
  new PrintWriter(output){write(words.mkString("\n") + "\n"); close;}
  println("Wrote sorted wordlist to " + output)
}

def profileCorpus(c: edu.holycross.shot.ohco2.Corpus): Unit = {
  writeWordList(c)
  writeParse("words.txt")

  val txts = c.nodes.map(_.text)
  val tokens = txts.map(_.split(" ")).toVector.flatten

  val parseFile = "output.txt"

  println("\n\nProfile")
  println(  "-------")
  println("Citable text units: " + c.size)
  println("Tokens: " + tokens.size)
  println("Distinct tokens: " + tokens.distinct.size)
  println("Now analyze parses in " + parseFile)

}

println("\n\nThings you can do with this script:\n")

println("Compile a fresh parser:\n")
println("\tcompile([TABULAEDIR])\n\n")

println("Parse a list of words:\n")
println("\tparse(WORDSFILE)\n\n")

println("Parse a list of words and write results to a file:\n")
println("\twriteParse(WORDSFILE, [OUTPUTFILE])")
