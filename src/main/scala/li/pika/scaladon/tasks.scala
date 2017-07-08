package li.pika.scaladon
package tasks

import javax.script.{Bindings, SimpleBindings}

import scala.collection.JavaConverters._
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.interpreter.Results.{Incomplete, Success}

import errors._


sealed trait Task extends (() => Unit)

object Task {
  def defaultSettings(): Settings = {
    val settings = new Settings
    settings.deprecation.value = true
    settings.feature.value = true
    settings.usejavacp.value = true
    settings
  }
}


case class RunScript(source: inputs.Input,
                     bindings: Bindings = new SimpleBindings(),
                     settings: Settings = Task.defaultSettings())
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


case class GoInteractive(prompt: Option[String] = None,
                         welcome: Option[String] = None,
                         settings: Settings = Task.defaultSettings())
  extends Task {
  def apply() {
    val oldPrompt = prompt.map(_ => System.getProperty("scala.repl.prompt"))
    prompt.map(System.setProperty("scala.repl.prompt", _))
    val oldWelcome = welcome.map(_ => System.getProperty("scala.repl.welcome"))
    welcome.map(System.setProperty("scala.repl.welcome", _))
    try {
      val scala = new ILoop
      scala.process(settings)
    } finally {
      oldPrompt.map(System.setProperty("scala.repl.prompt", _))
      oldWelcome.map(System.setProperty("scala.repl.welcome", _))
    }
  }
}
