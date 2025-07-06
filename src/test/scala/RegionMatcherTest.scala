import org.scalatest.funsuite.AnyFunSuite
import utility.IO.{readLocations, readRegions}
import utility.RegionMatcher.matchLocationsWithRegions

class RegionMatcherTest extends AnyFunSuite {
  test("Match regions - empty results") {
    val relativePath = os.pwd / os.RelPath("src/test/scala/input")
    val locationsFile = relativePath / "MRER_Locations.json"
    val regionsFile =  relativePath / "MRER_Regions.json"
    val locations = readLocations(locationsFile.toString)
    val regions = readRegions(regionsFile.toString)

    val results = matchLocationsWithRegions(locations, regions)
    assert(results.isEmpty, "Results were populated with empty entries\n" +
      "Expected: No entries - empty result list\n" +
      "Actual: Entries exist - result list is populated with empty entries\n")
  }

  test("Match regions - not empty results") {
    val relativePath = os.pwd / os.RelPath("src/test/scala/input")
    val locationsFile = relativePath / "MRNER_Locations.json"
    val regionsFile =  relativePath / "MRER_Regions.json"
    val locations = readLocations(locationsFile.toString)
    val regions = readRegions(regionsFile.toString)

    val results = matchLocationsWithRegions(locations, regions)
    assert(results.length == 1, "Results were not populated\n" +
      "Expected: One entry\n" +
      s"Actual: ${results.length} entry\n")
  }

  test("Match regions - multiple location matches") {
    val relativePath = os.pwd / os.RelPath("src/test/scala/input")
    val locationsFile = relativePath / "MRMLM_Locations.json"
    val regionsFile = relativePath / "MRMLM_Regions.json"
    val locations = readLocations(locationsFile.toString)
    val regions = readRegions(regionsFile.toString)

    val results = matchLocationsWithRegions(locations, regions)

    assert(results.length == 4, "Results were not populated correctly\n" +
      "Expected: 4 entries\n" +
      s"Actual: ${results.length} entry\n")

    assert(results(2).matchedLocations.length == 2, "Region did not had multiple locations matched\n" +
      "Expected: 2 entries\n" +
      s"Actual: ${results.length} entries\n")

    assert(results(2).matchedLocations.head == results(1).matchedLocations.head,
      "A location was not matched with multiple regions\n" +
      s"${results(2).matchedLocations.head} is equal to ${results(1).matchedLocations.head}")
  }
}
