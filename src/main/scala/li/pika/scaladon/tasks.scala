package li.pika.scaladon
package tasks

import java.io.File
import scala.tools.nsc.{GenericRunnerSettings, ScriptRunner, Settings}
import scala.tools.nsc.interpreter.ILoop

import errors._


sealed trait Task extends (() => Unit)

object Task {
  val defaultWriter: ((String) => Unit) = Console.err.println

  def settings(forMessages: ((String) => Unit)
                            = defaultWriter): GenericRunnerSettings = {
    val settings = new GenericRunnerSettings(forMessages)
    settings.deprecation.value = true
    settings.feature.value = true
    settings.usejavacp.value = true
    settings.nc.value = true
    settings
  }
}


case class RunScript(source: File,
                     arguments: Seq[String] = Seq(),
                     settings: GenericRunnerSettings = Task.settings())
  extends Task {

  def apply() {
    val runner = new ScriptRunner
    if (!runner.runScript(settings, source.getPath, arguments.toList)) {
      throw ScalaExecutionErr(source.getPath)
    }
  }
}


case class RunInteractive(prompt: Option[String] = None,
                          welcome: Option[String] = None,
                          settings: Settings = Task.settings())
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
