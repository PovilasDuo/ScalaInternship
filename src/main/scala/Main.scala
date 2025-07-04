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
  val source = Source.fromFile(filePath)
  val content = source.mkString
  read[List[Location]](content)
}

def readRegions(filePath: String) : List[Region] = {
  val source = Source.fromFile(filePath)
  val content = source.mkString
  read[List[Region]](content)
}

def writeResultsFile(results:List[Results], outputFile: String): Unit = {
  val path = os.Path("C:\\Users\\Neda\\Desktop\\Povilo\\ScalaInternship\\ScalaInternship\\output" + outputFile)
  if os.exists(path) then os.remove(path)
  os.write(path, write(results, indent = 2))
}

def matchLocationsWithRegions(locations: List[Location], regions: List[Region]): List[Results]  = {
  regions.map { region =>
    val matched = locations.filter { loc =>
      region.polygon.exists { polygon =>
        if (polygon.points.length >= 3) {
          WindingNumberPointPolygon(polygon, loc.coordinates)!= 0
        } else false
      }
    }
    Results(region.name, matched.map(_.name))
  }.filter(_.matchedLocations.nonEmpty)
}



