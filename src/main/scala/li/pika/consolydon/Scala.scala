package li.pika.consolydon

import javax.script.{Bindings, SimpleBindings}
import scala.collection.JavaConverters._
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.interpreter.Results._

import errors._


object Scala {
  def defaultSettings(): Settings = {
    val settings = new Settings
    settings.deprecation.value = true
    settings.feature.value = true
    settings.usejavacp.value = true
    settings
  }
}

case class Scala(source: inputs.Scala,
                 bindings: Bindings = new SimpleBindings(),
                 settings: Settings = Scala.defaultSettings())
  extends Task {

  lazy val scala: ILoop = {
    val scala = new ILoop
    scala.settings = settings
    scala.createInterpreter()
    scala.intp.initializeSynchronous()
    if (scala.intp.reporter.hasErrors) throw ScalaSetupErr()
    for ((k, v) <- bindings.asScala.toSeq) {
      val t = v.getClass.getCanonicalName
      scala.bind(k, t, v)
    }
    scala
  }

  @throws[Base]
  def apply() {
    // Similar to code in: `scala.tools.nsc.interpreter.ILoop.pasteCommand`.
    // The `scala.tools.nsc.ScriptRunner` utility has given me some problems:
    //  * Compiling in a scope in which a root object `Hi` is missing in a
    //    former version of this code. Makes sense because it invokes an
    //    external "compile server".
    //  * Getting stuck -- not terminating/returning -- when I tried to use it
    //    at a later time. Probably not using it right but there was nothing
    //    obviously wrong about the invocation.
    scala.withLabel(source.label) {
      scala.beQuietDuring {
        val result = scala.intp.interpret(source.text)
        result match {
          case Success => {}
          case Incomplete => {
            throw ScalaExecutionErr(source.label, "Incomplete program text")
          }
          case _ => {
            throw ScalaExecutionErr(source.label, s"Failed run ('${result}')")
          }
        }
      }
    }
  }
}
