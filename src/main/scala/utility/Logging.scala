package utility

import com.typesafe.scalalogging.Logger

object Logging {
  private val logger = Logger("ScalaInternship")
  
  def logError(message: String): Unit = {
    logger.error(message)
  }

  def logInfo(message: String): Unit = {
    logger.info(message)
  }
}
