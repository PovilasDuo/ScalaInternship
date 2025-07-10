package models

import cats.data.{Validated, ValidatedNel}
import upickle.default.*
import cats.implicits.*
import utility.JsonUtility.extractField

case class Location (name: String, coordinates: Point) {
  require(name.nonEmpty, "Location 'name' can not be empty")
}

object Location {
  private def fromList(name: String, coordinatesList: List[Double]) : ValidatedNel[String, Location] =
    coordinatesList match {
      case x::y::Nil => Validated.validNel(Location(name, Point(x,y)))
      case _ => Validated.invalidNel("Expected exactly two coordinates (x, y)")
  }

  implicit val rw: ReadWriter[Location] = readwriter[ujson.Value].bimap[Location](
    location => ujson.Obj(
      "name" -> location.name,
      "coordinates" -> ujson.Arr(location.coordinates.x, location.coordinates.y)
    ),

    json => {
      val obj = json.obj

      val validatedLocation: ValidatedNel[String, Location] = (
        extractField(obj, "name")(_.str),
        extractField(obj, "coordinates")(_.arr.map(_.num).toList)
      ).mapN(fromList).andThen(identity)

      validatedLocation match {
        case Validated.Valid(location) => location
        case Validated.Invalid(errors) =>
          throw new Exception(s"Failed to decode Location: ${errors.toList.mkString(", ")}")
      }
    }
  )
}

