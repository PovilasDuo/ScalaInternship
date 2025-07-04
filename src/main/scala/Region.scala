import upickle.default.*

case class Region(name: String, polygon: List[Polygon]) {
  require(name.nonEmpty, "Location 'name' can not be empty")
  require(polygon.nonEmpty, "Region can not have no polygons")
}

object Region {
  implicit val rw: ReadWriter[Region] = readwriter[ujson.Value].bimap[Region](
    reg => ujson.Obj(
      "name" -> reg.name,
      "coordinates" -> ujson.Arr(
        reg.polygon.map { polygon =>
          ujson.Arr(
            polygon.points.map { point =>
              ujson.Arr(point.x, point.y)
            }
          )
        }
      )
    ),

    json => {
      val obj = json.obj

      if (obj.value.contains("coordinates") && obj.value.contains("name")) {
        val name: String = obj("name").str
        val polygons = obj("coordinates").arr.map { polygonGroup =>
          val points = polygonGroup.arr.map {
            case ujson.Arr(values) if values.length == 2 =>
              val x = values(0).num.toInt
              val y = values(1).num.toInt
              Point(x, y)
            case other => throw new Exception(s"Expected longitude and latitude got $other")
          }.toList
          Polygon.safePolygon(points) match {
            case Some(polygon) => Polygon(points)
            case None => throw new Exception("Invalid input: less than 3 points")
          }
        }.toList
        Region(name, polygons)
      }
      else {
        throw new Exception("Input JSON does not contain the required structure")
      }
    }
  )
}
