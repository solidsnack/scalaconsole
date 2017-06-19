package li.pika.hi

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.interpreter.Results._


object Hi {
  def main(args: Array[String]) {
    interpreter(args)
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
        err("Interpreter encountered errors during initialization!")
        return
      }
      for (path <- paths) {
        scala.withFile(path) { f =>
          val result = scala.intp.interpret(f.slurp())
          result match {
            case Success => info(s"${path}: ${result}")
            case _ => {
              err(s"${path}: Unsuccessful (${result}) interpretation.")
              return
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

  def err(message: String): Unit = {
    Console.err.println(s"[${Console.RED}error${Console.RESET}] " + message)
  }

  def info(message: String): Unit = {
    Console.err.println("[info] " + message)
  }
}
