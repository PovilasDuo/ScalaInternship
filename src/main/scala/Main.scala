import models.Config
import utility.IO.{readLocations, readRegions, writeResultsFile}
import utility.InputParser
import utility.Logging.logError
import utility.RegionMatcher.matchLocationsWithRegions

object Main {
  def main(args: Array[String]): Unit = {
    val argMap: Config = InputParser.parseInputs(args) match {
      case Some(config) => config
      case None =>
        logError("Invalid input arguments. Use --help to see usage")
        sys.exit(1)
    }

    val locations = readLocations(argMap.locations)
    val regions = readRegions(argMap.regions)

    val results = matchLocationsWithRegions(locations, regions)
    writeResultsFile(results, argMap.output)
  }
}