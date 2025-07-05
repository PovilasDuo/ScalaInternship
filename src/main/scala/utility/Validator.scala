package utility

import models.Results
import os.Path

import java.nio.file.Paths

object Validator {
  def validateInput(args: Array[String]): Map[String, String] = {
    val (validArguments, invalidArguments) = args.partition {
      arg => arg.startsWith("--") && arg.contains("=")
    }

    if (validArguments.length != 3) {
      throw new IllegalArgumentException(
        s"Expected region, location and output json file arguments.\n" +
          s"Received valid arguments: ${validArguments.mkString("", ", ", "")}\n" +
          s"Received invalid arguments: ${invalidArguments.mkString("", ", ", "")}\n")
    }

    validArguments.map(_.split("=", 2)).collect {
      case Array(key, value) if key.startsWith("--") => key.drop(2) -> value
    }.toMap
  }

  def validateOutput(resultsList: List[Results]): Unit = {
    if (resultsList.isEmpty) println("None of the locations matched with the regions")
  }

  def checkIfFilePathIsRelative(filePath: String): Boolean = {
    !Paths.get(filePath).isAbsolute
  }
}
