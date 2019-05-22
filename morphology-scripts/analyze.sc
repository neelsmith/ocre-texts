
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


// Create corpus  of orthographically valid texts
def corpus(fileName: String = currentSource) : Corpus = {
  msg("Creating corpus composed of orthographically valid texts..")
  val allTexts = OcreUtilities.loadCorpus(fileName)
  OcreUtilities.goodOnly(allTexts)
}

// Convert SFST output to objects
def morph(src: String = "newparse.txt") :  Vector[AnalyzedToken ]= {
  FstFileReader.formsFromFile(src)
}

// Find morphological analysis for a given token:
def lysis(tkn: String, parses: Vector[AnalyzedToken] = morph()) : Option[AnalyzedToken] = {
  val matches = parses.filter(_.token == tkn)
  matches.size match {
    case 1 => Some(matches(0))
    case _ => {
      println("FAILED TO FIND ANALYSIS for " + tkn)
      None
    }
  }
}


// tokenize corpus
def tokens (c: Corpus = corpus()) :  Vector[MidToken]= {
  msg("Tokenizing valid texts..")
  val allTokens = NormalizedLegendOrthography.tokenizeCorpus(c)
  msg("Done.")
  allTokens
}

def lysisLemmaV(parses: Vector[AnalyzedToken]) : String = {
  parses.map(_.analyses).flatten.map(_.lemmaId).distinct.mkString(", ")
}


def lysisLemma(tkn: AnalyzedToken) : String = {
  tkn.analyses.map(_.lemmaId).distinct.mkString(", ")
}

def tknCex(tkns: Vector[MidToken], parses: Vector[AnalyzedToken]) = {
  val rows = for (t <- tkns) yield {
    t.tokenCategory.get match {
      case LexicalToken => {
        val morph = lysis(t.string, parses).get
        println(morph.analyses)
        s"${t.string}#LEX#${lysisLemma(morph)}"
      }
      case NumericToken => s"${t.string}#NUM#"
      case _ => "UNRECOGNIZED CAT FOR: " + t
    }
  }
  rows.toVector
}

def analyzeTokens(tkns: Vector[MidToken]) = {
  val x = for (t <- tkns) yield {
    t.tokenCategory.get match {
      case LexicalToken => "LEX"
      case NumericToken => "NUM"
      case _ => "UNRECOGNIZED CAT FOR: " + t
    }
  }
  x
}

// Select lexical tokens only from a corpus
def lex(c: Corpus = corpus()) :  Vector[MidToken]= {
  val allTokens = tokens(c)
  allTokens.filter(_.tokenCategory == Some(LexicalToken))
}

// Create alphabetical word list from list of lexical tokens
def forms(lexTokens : Vector[MidToken] = lex()): Vector[String] = {
  lexTokens.map(_.string).distinct.sorted
}
