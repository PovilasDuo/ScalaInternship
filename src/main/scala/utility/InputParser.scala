package utility

import models.Config
import scopt.OParser

object InputParser {
  def parseInputs(args: Array[String]): Option[Config] = {
    val builder = OParser.builder[Config]
    val parser = {
      import builder.*
      OParser.sequence(
        programName("ScalaInternship"),
        head("ScalaInternship", "1.0"),

        opt[String]('l', "locations")
          .required()
          .action((x, c) => c.copy(locations = x))
          .validate(path =>
            if (path.endsWith(".json")) success
            else failure("Must be a .json file"))
          .text("Path to the locations JSON file"),
        opt[String]('r', "regions")
          .required()
          .action((x, c) => c.copy(regions = x))
          .validate(path =>
            if (path.endsWith(".json")) success
            else failure("Must be a .json file"))
          .text("Path to the regions JSON file"),
        opt[String]('o', "output")
          .required()
          .action((x, c) => c.copy(output = x))
          .validate(path =>
            if (path.endsWith(".json")) success
            else failure("Must be a .json file"))
          .text("Path to the output JSON file"),

          help("help").text("Provide two input files: 'locations.json' 'regions.json'; one output file 'output.json'")
      )
    }
    OParser.parse(parser, args, Config())
  }
}
