package edu.holycross.shot.ocre

import edu.holycross.shot.mid.validator._

import edu.holycross.shot.tabulae._

trait TokenAnalysis {
  def token: String
  def tokenCategory:  MidTokenCategory
}

case class LexicalAnalysis(string: String, category: MidTokenCategory, tkn: AnalyzedToken) extends TokenAnalysis  {
  def token = string
  def tokenCategory = category
}

case class NumericAnalysis(string: String, category: MidTokenCategory, numericValue : Int) extends TokenAnalysis  {
  def token = string
  def tokenCategory = category
}
