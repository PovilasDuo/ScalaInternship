ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

Compile / run / mainClass := Some("Main")

lazy val root = (project in file("."))
  .settings(
    name := "ScalaInternship",
    libraryDependencies += "com.lihaoyi" %% "upickle" % "4.0.2",
    libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.11.2"
)
