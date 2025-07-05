package models

import upickle.default.*

case class Results(region: String, matchedLocations: List[String]) derives ReadWriter {
  require(region.nonEmpty, "Region identifier can not be empty")
}
