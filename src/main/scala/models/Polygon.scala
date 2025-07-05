package models

case class Polygon private (points: List[Point]) {
  require(points.nonEmpty, "Polygon must not have an empty list of points")
  require(points.length >= 3, s"Polygon must contain at least 3 points not ${points.length}")
}

object Polygon {
  def fromPoints(points: List[Point]) : Option[Polygon] = {
    if (points.nonEmpty && points.length >= 3) Some(Polygon(points)) else None
  }
}