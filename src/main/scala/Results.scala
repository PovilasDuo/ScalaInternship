import upickle.default.*

case class Results(region: String, matchedLocations: List[String]) derives ReadWriter
