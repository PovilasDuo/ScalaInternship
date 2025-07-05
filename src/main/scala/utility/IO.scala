package utility

import models.{Location, Region, Results}
import upickle.default.*
import scala.io.Source

object IO {
  def readLocations(filePath: String): List[Location] = {
    if (os.exists(os.Path(filePath))) {
      val source = Source.fromFile(filePath)
      val content =
        try source.mkString
        catch case _: Throwable => throw new Exception("Failed processing the file")
        finally source.close()

      val originalList = read[List[Location]](content)
      val distinctList = originalList.distinctBy(_.name)

      if (!(originalList sameElements distinctList)) {
        println("Duplicate regions found. Accepting the first entry")
      }
      distinctList
    }
    else {
      throw new NoSuchElementException("Location file was not found")
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
        println("Duplicate locations found. Accepting the first entry")
      }
      distinctList
    }
    else {
      throw new NoSuchElementException("Region file was not found")
    }
  }

  def writeResultsFile(results: List[Results], outputFile: String): Unit = {
    val path = os.Path(os.pwd.toString + "/output/" + outputFile)
    if os.exists(path) then os.remove(path)
    os.write(path, write(results, indent = 2))
  }
}
