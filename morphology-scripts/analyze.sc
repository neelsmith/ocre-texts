
import edu.holycross.shot.tabulae.builder._
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

import sys.process._
import scala.language.postfixOps


import edu.holycross.shot.tabulae._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source
import edu.holycross.shot.mid.validator._
import edu.holycross.shot.ocre._
import java.io.PrintWriter
import sys.process._
import scala.language.postfixOps


// expect this script to be loaded from root directory of repo
val currentSource = "ocre-data/normalized15.cex"


def msg(txt: String): Unit  = {
  println("\n\n")
  println(txt)
  println("\n")
}


// Create corpus  of orthographyically valid texts
def corpus(fileName: String = currentSource) : Corpus = {
  msg("Creating corpus composed of orthographically valid texts..")
  val allTexts = OcreUtilities.loadCorpus(fileName)
  OcreUtilities.goodOnly(allTexts)
}

def lex(c: Corpus = corpus()) :  Vector[MidToken]= {
  msg("Tokenizing valid texts..")
  val allTokens = NormalizedLegendOrthography.tokenizeCorpus(c)
  msg("Done.")
  allTokens.filter(_.tokenCategory == Some(LexicalToken))
}

def forms(lexTokens : Vector[MidToken] = lex()): Vector[String] = {
  lexTokens.map(_.string).distinct.sorted
}
