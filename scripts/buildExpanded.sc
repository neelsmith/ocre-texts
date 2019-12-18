import edu.holycross.shot.ocre._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._


// collect all mappings, generate expanded corpus:
val expanded = TextExpander.expandFromMappingsDir(21, "mappings")
// isolate corpus of orthographically valid expanded texts
val newCorpus = TextExpander.validCorpus(expanded)
val badCorpus = TextExpander.invalidCorpus(expanded)

// write it to disk in CEX format:
import java.io.PrintWriter
new PrintWriter("ric-1-3-valid-ortho.cex"){ write(newCorpus.cex()); close;}
new PrintWriter("ric-1-3-bad-ortho.cex"){ write(badCorpus.cex()); close;}
