name := "ocre-texts"
organization := "edu.holycross.shot"
version := "0.3.1"
licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))

crossScalaVersions in ThisBuild := Seq("2.11.8", "2.12.4")
scalaVersion := (crossScalaVersions in ThisBuild).value.last
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")
resolvers +=  Resolver.bintrayRepo("cibotech", "public")
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "edu.holycross.shot.cite" %% "xcite" % "4.0.2",
  "edu.holycross.shot" %% "ohco2" % "10.17.0",
  "edu.holycross.shot" %% "nomisma" % "2.2.0",
  "edu.holycross.shot" %% "latphone" % "2.5.2",

  "edu.holycross.shot" %% "midvalidator" % "6.1.3",

  "edu.holycross.shot" %% "tabulae" % "3.4.0",
  "com.github.pathikrit" %% "better-files" % "3.5.0",
  "org.wvlet.airframe" %% "airframe-log" % "19.8.10"
)

/*
tutSourceDirectory := file("tut")
tutTargetDirectory := file("docs")

enablePlugins(TutPlugin)
*/
