name := """Scala-Play-Quickstart"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "test",
  jdbc,
  "org.sorm-framework" % "sorm" % "0.3.19"
)
dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value /* SORM fix */

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
