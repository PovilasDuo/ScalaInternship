import upickle.default.*

case class Region(name: String, polygon: List[Polygon])

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
      val name = obj("name").str
      val polygons = obj("coordinates").arr.map { polygonGroup =>
        val points = polygonGroup.arr.map {
          case ujson.Arr(values) if values.length == 2 =>
            val x = values(0).num.toInt
            val y = values(1).num.toInt
            Point(x, y)
          case other => throw new Exception(s"Expected X and Y got $other")
        }.toList
        Polygon(points)
      }.toList
      Region(name, polygons)
    }
  )
}
