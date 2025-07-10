package models

import cats.data.{Validated, ValidatedNel}
import upickle.default.*
import cats.implicits.*
import utility.JsonUtility.extractField

case class Region(name: String, polygons: List[Polygon]) {
  require(name.nonEmpty, "Region 'name' can not be empty")
  require(polygons.nonEmpty, "Region can not have no polygons")
}

object Region {
  implicit val rw: ReadWriter[Region] = readwriter[ujson.Value].bimap[Region](
    region => ujson.Obj(
      "name" -> region.name,
      "coordinates" -> ujson.Arr(
        region.polygons.map { polygon =>
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

      val validatedRegion: ValidatedNel[String, Region] = (
        extractField(obj, "name")(_.str),
        extractField(obj, "coordinates")(_.arr.toList).andThen { polygonList =>
         polygonList.traverse { polygonGroup =>
            val pointsValidated: ValidatedNel[String, List[Point]] = polygonGroup.arr.toList.traverse {
              case ujson.Arr(values) if values.length == 2 =>
                val List(x: ujson.Value, y: ujson.Value) = values.toList
                (
                  Validated.condNel(x.value.isInstanceOf[Double], x.num, s"Invalid x: $x"),
                  Validated.condNel(y.value.isInstanceOf[Double], y.num, s"Invalid y: $y")
                ).mapN(Point.apply)
              case other => Validated.invalidNel(s"Expected [x, y], got: $other")
            }
            pointsValidated.andThen(Polygon.fromPoints)
          }
        }
      ).mapN(Region.apply)

      validatedRegion match {
        case Validated.Valid(region) => region
        case Validated.Invalid(errors) => throw new Exception(
          s"Failed to decode Region: ${errors.toList.mkString(", ")}")
      }
    }
  )
}