package utility

import models.WindingNumber.windingNumberPointPolygon
import models.{Location, Region, Results}

object RegionMatcher {

  def matchLocationsWithRegions(locations: List[Location], regions: List[Region]): List[Results] = regions.map {
    region =>
      val matched = locations.filter { loc =>
        region.polygons.exists {
          polygon =>
            windingNumberPointPolygon(polygon, loc.coordinates) != 0
        }
      }
      Results(region.name, matched.map(_.name))
  }.filter(_.matchedLocations.nonEmpty)
}
