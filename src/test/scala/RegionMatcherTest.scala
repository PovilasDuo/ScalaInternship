import TestUtility.readResourceFile
import cats.data.Validated
import models.{Location, Point, Polygon, Region}
import org.scalatest.funsuite.AnyFunSuite
import utility.IO.{readLocations, readRegions}
import utility.RegionMatcher.matchLocationsWithRegions

class RegionMatcherTest extends AnyFunSuite {
  def loadTestData(locationFile: String, regionFile: String): (List[Location], List[Region]) = {
    val locationsFile = readResourceFile(locationFile)
    val regionsFile = readResourceFile(regionFile)
    (readLocations(locationsFile), readRegions(regionsFile))
  }

  test("Match regions - empty results") {
    val (locations, regions) = loadTestData("tests/MRER_Locations.json","tests/MRER_Regions.json")
    val results = matchLocationsWithRegions(locations, regions)

    assert(results.isEmpty, "Results were populated with empty entries\n" +
      "Expected: No entries - empty result list\n" +
      "Actual: Entries exist - result list is populated with empty entries\n")
  }

  test("Match regions - not empty results") {
    val files = loadTestData("tests/MRNER_Locations.json", "tests/MRER_Regions.json")
    val results = matchLocationsWithRegions(files._1, files._2)

    assert(results.length == 1, "Results were not populated\n" +
      "Expected: One entry\n" +
      s"Actual: ${results.length} entry\n")
  }

  test("Match regions - multiple location matches") {
    val files = loadTestData("tests/MRMLM_Locations.json", "tests/MRMLM_Regions.json")
    val results = matchLocationsWithRegions(files._1, files._2)

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

  def createTestData(): (Polygon, Region) = {
    val polygon: Polygon = Polygon.fromPoints(List(Point(0, 0), Point(2, 0), Point(2, 2), Point(0, 2))) match {
      case Validated.Valid(polygon) => polygon
      case Validated.Invalid(errors) => throw new Exception(
        s"Failed to decode Region: ${errors.toList.mkString(", ")}")
    }
    (polygon, Region("Square", List(polygon)))
  }

  test("Match regions - location exactly on polygon edge is matched") {
    val (polygon, region) = createTestData()
    val locOnEdge = Location("OnEdge", Point(1, 0))
    val results = matchLocationsWithRegions(List(locOnEdge), List(region))
    assert(results.exists(_.matchedLocations.contains("OnEdge")))
  }

  test("Match regions - location exactly on polygon vertex is matched") {
    val (polygon, region) = createTestData()
    val locOnVertex = Location("OnVertex", Point(0, 0))
    val results = matchLocationsWithRegions(List(locOnVertex), List(region))
    assert(results.exists(_.matchedLocations.contains("OnVertex")))
  }

  test("Match regions - location just outside polygon is not matched") {
    val (polygon, region) = createTestData()
    val locOutside = Location("Outside", Point(2.0001, 1))
    val results = matchLocationsWithRegions(List(locOutside), List(region))
    assert(!results.exists(_.matchedLocations.contains("Outside")))
  }
}
