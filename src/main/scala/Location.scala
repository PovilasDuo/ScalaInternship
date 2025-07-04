import upickle.default.*

case class Location(name: String, coordinates: Point) {
  require(name.nonEmpty, "Location 'name' can not be empty")
}

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

      if (obj.value.contains("coordinates") && obj.value.contains("name")) {
        val name = obj("name").str
        val coords = obj("coordinates").arr.map(_.num.toInt).toList
        fromList(name, coords)
      }
      else {
        throw new Exception("Location input JSON does not contain the required structure")
      }
    }
  )
}

