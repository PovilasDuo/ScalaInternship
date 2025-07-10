ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

Compile / run / mainClass := Some("Main")

lazy val root = (project in file("."))
  .settings(
    name := "ScalaInternship",
    libraryDependencies += "com.lihaoyi" %% "upickle" % "4.0.2",
    libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.11.2",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test,
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.11",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.1.0",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.12.0"
  )
