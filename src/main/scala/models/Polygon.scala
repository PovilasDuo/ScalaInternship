package models

import cats.data.{Validated, ValidatedNel}

case class Polygon private (points: List[Point]) {
  require(points.nonEmpty, "Polygon must not have an empty list of points")
  require(points.length >= 3, s"Polygon must contain at least 3 points not ${points.length}")
}

object Polygon {
  def fromPoints(points: List[Point]) : ValidatedNel[String, Polygon] = {
    if (points.nonEmpty && points.length >= 3) Validated.validNel(Polygon(points))
    else Validated.invalidNel("List of points is empty or contains less than 3 points")
  }
}