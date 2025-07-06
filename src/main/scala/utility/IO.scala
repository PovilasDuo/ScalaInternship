package utility

import models.{Location, Region, Results}
import os.Path
import upickle.default.*
import utility.Validator.*
import java.nio.file.NoSuchFileException
import scala.io.Source

object IO {
  private def readList[A: Reader](filePath: String, key: A => String): List[A] = {
    val originalList = read[List[A]](readFile(filePath))
    val distinctList = originalList.distinctBy(key)

    if (!(originalList sameElements distinctList)) {
      println("Duplicate regions found. Accepting the first entry")
    }
    distinctList
  }

  private def readFile(filePath: String): String = {
    val correctFilePath: Path = if checkIfFilePathIsRelative(filePath) then makePathsAbsolute(filePath)
    else os.Path(filePath)

    if (os.exists(correctFilePath)) {
      val source = Source.fromFile(filePath)
      try source.mkString
      catch case _: Throwable => throw new Exception("Failed processing the file")
      finally source.close()
    }
    else {
      throw new NoSuchFileException(s"Input file was not found with such path: $filePath")
    }
  }

  def makePathsAbsolute(filePath: String): Path = {
    os.pwd / filePath
  }

  def readLocations(filePath: String): List[Location] = {
    readList[Location](filePath, _.name)
  }

  def readRegions(filePath: String): List[Region] = {
    readList[Region](filePath, _.name)
  }

  def writeResultsFile(results: List[Results], outputFile: String): Unit = {
    val path = os.pwd / os.RelPath(outputFile)
    if os.exists(path) then os.remove(path)
    os.write(path, write(results, indent = 2))
  }
}
