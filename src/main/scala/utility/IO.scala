package utility

import models.{Location, Region, Results}
import os.Path
import upickle.default.*
import utility.Validator.*

import java.nio.file.NoSuchFileException
import scala.io.Source

object IO {
  def readLocations(filePath: String): List[Location] = {
    val correctFilePath: Path = if checkIfFilePathIsRelative(filePath) then makePathsAbsolute(filePath)
    else os.Path(filePath)

    if (os.exists(correctFilePath)) {
      val source = Source.fromFile(filePath)
      val content =
        try source.mkString
        catch case _: Throwable => throw new Exception("Failed processing the file")
        finally source.close()

      val originalList = read[List[Location]](content)
      val distinctList = originalList.distinctBy(_.name)

      if (!(originalList sameElements distinctList)) {
        println("Duplicate locations found. Accepting the first entry")
      }
      distinctList
    }
    else {
      throw new NoSuchFileException(filePath)
    }
  }

  def readRegions(filePath: String): List[Region] = {
    if (os.exists(os.Path(filePath))) {
      val source = Source.fromFile(filePath)
      val content =
        try source.mkString
        catch case _: Throwable => throw new Exception("Failed processing the file")
        finally source.close()

      val originalList = read[List[Region]](content)
      val distinctList = originalList.distinctBy(_.name)
      if (!(originalList sameElements distinctList)) {
        println("Duplicate regions found. Accepting the first entry")
      }
      distinctList
    }
    else {
      throw new NoSuchFileException("Region file was not found")
    }
  }

  def writeResultsFile(results: List[Results], outputFile: String): Unit = {
    val path = os.Path(os.pwd.toString + "/output/" + outputFile)
    if os.exists(path) then os.remove(path)
    os.write(path, write(results, indent = 2))
  }
}
