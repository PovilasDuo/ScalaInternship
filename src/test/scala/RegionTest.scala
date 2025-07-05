import org.scalatest.funsuite.AnyFunSuite
import utility.IO.readRegions
import java.nio.file.NoSuchFileException

class RegionTest extends AnyFunSuite{
  test("Read Regions") {
    val regionJSON =
      """[ { "name": "Central Park", "coordinates": [ [ [-73.9731, 40.7644], [-73.9819, 40.7681],
                      [-73.9580, 40.8006], [-73.9492, 40.7968] ] ] },
                  { "name": "Downtown Dubai", "coordinates": [ [ [55.2744, 25.1972], [55.3080, 25.1972],
                      [55.3080, 25.2769], [55.2744, 25.2769] ] ] },
                  { "name": "Westminster", "coordinates": [ [ [-0.1420, 51.5069], [-0.1357, 51.5069],
                      [-0.1357, 51.5120], [-0.1420, 51.5120] ] ] } ]"""

    val path = os.Path(os.pwd.toString + "/input/" + "Regions.json")
    if os.exists(path) then os.remove(path)
    os.write(path, regionJSON)

    val regions = readRegions(path.toString)

    assert(regions.length == 3,
      "Not all regions were added when reading the file:" +
        "\nExpected: regions list length: 3" +
        s"\nActual: regions list length: ${regions.length}")
  }

  test("Read Regions - missing file") {
    val exception = intercept[NoSuchFileException](readRegions("test"))
  }

  test("Read Regions - duplicate names") {
    val regionJSON =
      """[ { "name": "Central Park", "coordinates": [ [ [-73.9731, 40.7644], [-73.9819, 40.7681], [-73.9580, 40.8006],
        [-73.9492, 40.7968] ] ] },
        { "name": "Central Park", "coordinates": [ [ [-73.9731, 40.7644], [-73.9819, 40.7681],
        [-73.9580, 40.8006], [-73.9492, 40.7968] ] ] },
        { "name": "Downtown Dubai", "coordinates": [ [ [55.2744, 25.1972], [55.3080, 25.1972], [55.3080, 25.2769],
        [55.2744, 25.2769] ] ] },
         { "name": "Westminster", "coordinates": [ [ [-0.1420, 51.5069], [-0.1357, 51.5069],
         [-0.1357, 51.5120], [-0.1420, 51.5120] ] ] } ]"""

    val path = os.Path(os.pwd.toString + "/input/" + "Regions.json")
    if os.exists(path) then os.remove(path)
    os.write(path, regionJSON)

    val regions = readRegions(path.toString)

    assert(regions.length == 3,
      "Duplicate regions with the same were added into the list" +
        "\nExpected: regions list length: 3 " +
        s"\nActual: regions list length: ${regions.length}")
  }
}
