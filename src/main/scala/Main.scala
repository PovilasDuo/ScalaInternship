import WindingNumber.WindingNumberPointPolygon
import scala.io.Source
import upickle.default.*

object Main {
  def main(args: Array[String]): Unit = {
    val argMap: Map[String, String] = args
      .map(_.split("=", 2))
      .collect { case Array(key, value) if key.startsWith("--") => key.drop(2) -> value }
      .toMap

    val regionFile = argMap.getOrElse("regions", sys.error("Missing --regions argument"))
    val locationFile = argMap.getOrElse("locations", sys.error("Missing --locations argument"))
    val outputFile = argMap.getOrElse("output", sys.error("Missing --output argument"))

    val locations = readLocations(locationFile)
    val regions = readRegions(regionFile)
    val results = matchLocationsWithRegions(locations, regions)

    writeResultsFile(results, outputFile)
  }
}

def readLocations(filePath: String) : List[Location] = {
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
    throw new Exception("Location file was not found")
  }
}

def readRegions(filePath: String) : List[Region] = {
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
    throw new Exception("Region file was not found")
  }
}

def writeResultsFile(results:List[Results], outputFile: String): Unit = {
  val path = os.Path(os.pwd.toString + "/output/"+ outputFile)
  if os.exists(path) then os.remove(path)
  os.write(path, write(results, indent = 2))
}

def matchLocationsWithRegions(locations: List[Location], regions: List[Region]): List[Results]  = {
  regions.map { region =>
    val matched = locations.filter { loc =>
      region.polygons.exists {
        polygon =>
          WindingNumberPointPolygon(polygon, loc.coordinates) != 0
        }
      }
    Results(region.name, matched.map(_.name))
    }.filter(_.matchedLocations.nonEmpty)
}