package li.pika.consolydon.errors

import scala.tools.nsc.Properties
import scala.util.matching.Regex


sealed trait Base extends Exception {
  val language: String
  val engine: String
  val source: String
}


/**
  * The engine tried to match a file name to an interpreter but was not able to
  * find a pattern that matched.
  */
case class FileMatchErr(source: String, patterns: Array[String])
  extends Exception(s"Couldn't match ${patterns.mkString(" ")} to: ${source}")
    with Base {
  val language: String = ""
  val engine: String = ""
}

object FileMatchErr {
  def apply(source: String, regexen: Regex*): FileMatchErr = {
    FileMatchErr(source, regexen.map(_.toString()).toArray)
  }
}


/**
  * There was no available engine for the target language.
  */
case class NoSuchEngineErr(language: String, source: String = "")
  extends Exception(s"Couldn't find a `${language}` engine for: ${source}")
    with Base {
  val engine: String = ""
}


/**
  * The Scala runner failed during startup or setup (not while executing
  * the target script).
  */
case class ScalaSetupErr(message: String = "Failed to setup Scala interpreter")
  extends Exception(message) with Base {
  val language: String = "scala"
  val engine: String = s"${Properties.versionMsg}"
  val source: String = ""
}


/**
  * The Scala runner was able to start successfully, but failed while executing
  * a script.
  */
case class ScalaExecutionErr(source: String, error: Option[String] = None)
  extends Exception(s"${error.getOrElse("Failed in")}: ${source}")
    with Base {
  val language: String = "scala"
  val engine: String = s"${Properties.versionMsg}"
}

object ScalaExecutionErr {
  def apply(source: String, error: String): ScalaExecutionErr = {
    ScalaExecutionErr(source, Some(error))
  }
}
