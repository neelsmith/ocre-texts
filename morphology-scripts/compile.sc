
import edu.holycross.shot.tabulae.builder._
import better.files._
import java.io.{File => JFile}
import better.files.Dsl._

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
