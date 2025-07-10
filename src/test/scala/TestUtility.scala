import utility.Logging.logInfo

object TestUtility {

  def cleanUpFiles(filesToRemove: List[os.Path]): Unit = {
    filesToRemove.foreach { path =>
      if (os.exists(path)) {
        try {
          os.remove(path)
        }
        catch {
          case _: Throwable => logInfo(s"Failed to remove $path")
        }
      }
    }
  }

  def readResourceFile(resourceName: String) : String = {
    Option(getClass.getClassLoader.getResource(resourceName))
      .map(url => os.Path(url.toURI).toString())
      .getOrElse(throw new IllegalArgumentException(s"Resource not found: $resourceName"))
  }
}
