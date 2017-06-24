package li.pika.hi

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.interpreter.Results._


object Hi {
  def main(args: Array[String]) {
    try {
      interpreter(args)
    } catch {
      case e: Err => {
        err(e.message)
        System.exit(1)
      }
    }
  }

  val internalValue = "This value is drawn from a class in the project."

  def interpreter(paths: Seq[String] = Seq()) {
    val settings = new Settings
    settings.deprecation.value = true
    settings.feature.value = true
    settings.usejavacp.value = true

    def interactive() {
      // Change the prompt and welcome message with properties:
      System.setProperty("scala.repl.welcome", "Welcome to Hi, the REPL demo!")
      System.setProperty("scala.repl.prompt", "%nhi> ")
      val scala = new ILoop
      scala.process(settings)
    }

    def batch() {
      // Similar to code in: `scala.tools.nsc.interpreter.ILoop.pasteCommand`.
      // There doesn't seem to be a clean way to access "batch mode
      // interpretation". The `scala.tools.nsc.ScriptRunner` utility would
      // compile in a scope in which `Hi` is missing.
      val scala = new ILoop
      scala.settings = settings
      scala.createInterpreter()
      scala.intp.initializeSynchronous()
      if (scala.intp.reporter.hasErrors) {
        throw Err("Interpreter encountered errors during initialization!")
      }
      for (path <- paths) {
        scala.withFile(path) { f =>
          scala.beQuietDuring {
            val result = scala.intp.interpret(f.slurp())
            result match {
              case Success => info(s"${path}: ${result}")
              case _ => throw Err(s"${path}: Unsuccessful (${result}) run.")
            }
          }
        }
      }
    }

    paths match {
      case Seq() => interactive()
      case _ => batch()
    }
  }

  def err(message: String) {
    Console.err.println(s"[${Console.RED}error${Console.RESET}] " + message)
  }

  def info(message: String) {
    Console.err.println("[info] " + message)
  }
}


case class Err(message: String) extends Exception(message)
