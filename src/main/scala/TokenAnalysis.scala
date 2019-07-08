package edu.holycross.shot.ocre

import edu.holycross.shot.mid.validator._

import edu.holycross.shot.tabulae._

trait SecondaryAnalysis {
  def token: String
  def tokenCategory:  MidTokenCategory
}

case class LexicalAnalysis(string: String, category: MidTokenCategory, tkn: AnalyzedToken) extends SecondaryAnalysis  {
  def token = string
  def tokenCategory = category
}

case class NumericAnalysis(string: String, category: MidTokenCategory)  extends SecondaryAnalysis  {
  // add , numericValue : Int)
  def token = string
  def tokenCategory = category
}
