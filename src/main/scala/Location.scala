import upickle.default.*
import upickle.default.{ReadWriter, macroRW, read, write}

case class Location(name: String, coordinates: Point)

object Location {
  private def fromList(name: String, coordinatesList: List[Int]) : Location = {
    require(coordinatesList.length == 2)
    Location(name, Point(coordinatesList.head, coordinatesList(1)))
  }

  implicit val rw: ReadWriter[Location] = readwriter[ujson.Value].bimap[Location](
    loc => ujson.Obj(
      "name" -> loc.name,
      "coordinates" -> ujson.Arr(loc.coordinates.x, loc.coordinates.y)
    ),

    json => {
      val obj = json.obj
      val name = obj("name").str
      val coords = obj("coordinates").arr.map(_.num.toInt).toList
      fromList(name, coords)
    }
  )
}

