name := "ocre-texts"
organization := "edu.holycross.shot"
version := "0.0.1"
licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))

crossScalaVersions in ThisBuild := Seq("2.11.8", "2.12.4")
scalaVersion := (crossScalaVersions in ThisBuild).value.last
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "edu.holycross.shot.cite" %% "xcite" % "3.7.0",
  "edu.holycross.shot" %% "ohco2" % "10.11.1",
  "edu.holycross.shot" %% "nomisma" % "0.3.0",
  "edu.holycross.shot" %% "latphone" % "2.1.1",

  "edu.holycross.shot" %% "midvalidator" % "5.2.1"
)

tutSourceDirectory := file("tut")
tutTargetDirectory := file("docs")

enablePlugins(TutPlugin)
