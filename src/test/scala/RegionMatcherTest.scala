import org.scalatest.funsuite.AnyFunSuite
import utility.IO.{readLocations, readRegions, writeResultsFile}
import utility.RegionMatcher.matchLocationsWithRegions

class RegionMatcherTest extends AnyFunSuite {
  test("Match regions - empty results") {
    val inputLocation = (os.pwd / "src" / "test" / "scala" / "input").toString
    val locationsFile = inputLocation + "/MRER_Locations.json"
    val regionsFile = inputLocation + "/MRER_Regions.json"
    val locations = readLocations(locationsFile)
    val regions = readRegions(regionsFile)

    val results = matchLocationsWithRegions(locations, regions)
    assert(results.isEmpty, "Results were populated with empty entries\n" +
      "Expected: No entries - empty result list\n" +
      "Actual: Entries exist - result list is populated with empty entries\n")
  }

  test("Match regions - not empty results") {
    val inputLocation = (os.pwd / "src" / "test" / "scala" / "input").toString
    val locationsFile = inputLocation + "/MRNER_Locations.json"
    val regionsFile = inputLocation + "/MRER_Regions.json"
    val locations = readLocations(locationsFile)
    val regions = readRegions(regionsFile)

    val results = matchLocationsWithRegions(locations, regions)
    assert(results.length == 1, "Results were not populated\n" +
      "Expected: One entry\n" +
      "Actual: Not one entry\n")
  }
}
