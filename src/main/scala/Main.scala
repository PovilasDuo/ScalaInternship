import utility.IO.{readLocations, readRegions, writeResultsFile}
import utility.Validator.{validateInput, validateOutput}
import utility.RegionMatcher.matchLocationsWithRegions

object Main {
  def main(args: Array[String]): Unit = {

    val argMap = validateInput(args)

    val regionFile = argMap.getOrElse("regions", sys.error("Missing --regions argument"))
    val locationFile = argMap.getOrElse("locations", sys.error("Missing --locations argument"))
    val outputFile = argMap.getOrElse("output", sys.error("Missing --output argument"))

    val locations = readLocations(locationFile)
    val regions = readRegions(regionFile)

    val results = matchLocationsWithRegions(locations, regions)
    validateOutput(results)
    writeResultsFile(results, outputFile)
  }
}