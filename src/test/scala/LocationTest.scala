import org.scalatest.funsuite.AnyFunSuite
import utility.IO.readLocations
import java.nio.file.NoSuchFileException

class LocationTest extends AnyFunSuite {
  test("Read Locations") {
    val locationJSON =
      """[ { "name": "Pub", "coordinates": [-0.1276, 51.5072] },
        { "name": "Bar", "coordinates": [-73.9855, 40.7580] } ]"""

    val path = os.Path(os.pwd.toString + "/input/" + "Locations.json")
    if os.exists(path) then os.remove(path)
    os.write(path, locationJSON)

    val locations = readLocations(path.toString)

    assert(locations.length == 2,
      "Not all locations were added when reading the file:" +
      "\nExpected: location list length: 2"+
      s"\nActual: location list length: ${locations.length}")
  }

  test("Read Locations - missing file") {
    val exception = intercept[NoSuchFileException](readLocations("test"))
  }

  test("Read Locations - duplicate names") {
    val locationJSON =
      """[
        { "name": "Pub", "coordinates": [-0.1276, 51.5072] },
        { "name": "Pub", "coordinates": [-0.1111, 54.5033] },
        { "name": "Bar", "coordinates": [-73.9855, 40.7580] }
      ]"""

    val path = os.Path(os.pwd.toString + "/input/" + "Locations.json")
    if os.exists(path) then os.remove(path)
    os.write(path, locationJSON)

    val locations = readLocations(path.toString)

    assert(locations.length == 2,
      "Duplicate locations with the same were added into the list" +
        "\nExpected: location list length: 2" +
        s"\nActual: location list length: ${locations.length}")
  }
}
