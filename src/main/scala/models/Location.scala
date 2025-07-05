package models

import upickle.default.*

case class Location private (name: String, coordinates: Point) {
  require(name.nonEmpty, "Location 'name' can not be empty")
}

object Location {

  private def fromList(name: String, coordinatesList: List[Double]) : Option[Location] = coordinatesList match {
    case x::y::Nil => Some(Location(name, Point(x,y)))
    case _ => None
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
        val coordinates = obj("coordinates").arr.map(_.num).toList

        fromList(name, coordinates) match {
          case Some(location) => location
          case None => throw new Exception("There was an error creating Location with given input")
        }
      }
      else {
        throw new Exception("Missing required fields in Location JSON")
      }
    }
  )
}

