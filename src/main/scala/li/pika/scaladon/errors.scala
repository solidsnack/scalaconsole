package li.pika.scaladon.errors



sealed trait Error extends Exception



/**
  * Failure while setting up the Scala system (before running any user code).
  */
case class ScalaSetupErr(message: String = "Failed to setup Scala interpreter")
  extends Exception(message) with Error


/**
  * The Scala runner was able to start successfully, but failed while executing
  * a script.
  */
case class ScalaExecutionErr(source: String, summary: String)
  extends Exception(s"${summary}: ${source}") with Error

object ScalaExecutionErr {
  def apply(source: String,
            error: Option[String] = None): ScalaExecutionErr = {
    ScalaExecutionErr(source, s"${error.getOrElse("Failed in")}: ${source}")
  }
}
