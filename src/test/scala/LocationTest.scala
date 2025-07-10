import TestUtility.cleanUpFiles
import org.scalatest.funsuite.AnyFunSuite
import utility.IO.readLocations
import java.nio.file.NoSuchFileException

class LocationTest extends AnyFunSuite {
  test("Read Locations") {
    val locationJSON =
      """[ { "name": "Pub", "coordinates": [-0.1276, 51.5072] },
        { "name": "Bar", "coordinates": [-73.9855, 40.7580] } ]"""

    val path = os.Path("Locations.json", os.pwd)
    if os.exists(path) then os.remove(path)
    os.write(path, locationJSON)

    val locations = readLocations(path.toString)

    assert(locations.length == 2,
      "Not all locations were added when reading the file:" +
      "\nExpected: location list length: 2"+
      s"\nActual: location list length: ${locations.length}")

    cleanUpFiles(List(path))
  }

  test("Read Locations - missing file") {
    val exception = intercept[NoSuchFileException](readLocations("test"))
  }

  test("Read Locations - duplicate names") {
    val locationJSON =
      """[{ "name": "Pub", "coordinates": [-0.1276, 51.5072] },
        { "name": "Pub", "coordinates": [-0.1111, 54.5033] },
        { "name": "Bar", "coordinates": [-73.9855, 40.7580] }]"""

    val path = os.Path("Locations.json", os.pwd)
    if os.exists(path) then os.remove(path)
    os.write(path, locationJSON)

    val locations = readLocations(path.toString)

    assert(locations.length == 2,
      "Duplicate locations with the same were added into the list" +
        "\nExpected: location list length: 2" +
        s"\nActual: location list length: ${locations.length}")

    cleanUpFiles(List(path))
  }

  test("Read Locations - missing 'name' field") {
    val badJson = """[{ "coordinates": [-0.1276, 51.5072] }]"""
    intercept[Exception](readLocations(badJson))
  }

  test("Read Locations - missing 'coordinates' field") {
    val badJson = """[{ "name": "NoCoords" }]"""
    intercept[Exception](readLocations(badJson))
  }

  test("Read Locations - coordinates length 1") {
    val badJson = """[{ "name": "BadCoord1", "coordinates": [-0.1276] }]"""
    intercept[Exception](readLocations(badJson))
  }

  test("Read Locations - coordinates length 3") {
    val badJson = """[{ "name": "BadCoord3", "coordinates": [-0.1276, 51.5072, 0] }]"""
    intercept[Exception](readLocations(badJson))
  }

  test("Read Locations - string coordinates") {
    val badJson = """[{ "name": "BadCoordStr", "coordinates": ["a", "b"] }]"""
    intercept[Exception](readLocations(badJson))
  }

  test("Read Locations - empty string name") {
    val badJson = """[{ "name": "", "coordinates": [-0.1276, 51.5072] }]"""
    intercept[IllegalArgumentException](readLocations(badJson))
  }
}
