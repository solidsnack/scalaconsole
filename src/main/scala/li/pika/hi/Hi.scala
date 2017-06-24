package li.pika.hi

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.interpreter.Results._


object Hi {
  case class Err(message: String) extends Exception(message)

  def main(args: Array[String]) {
    try {
      val interpreter = new Hi()
      args match {
        case Array() => interpreter.interactive()
        case _ => interpreter.batch(args)
      }
    } catch {
      case e: Err => {
        err(e.message)
        System.exit(1)
      }
    }
  }

  val internalValue = "This value is drawn from a class in the project."

  def err(message: String) {
    Console.err.println(s"[${Console.RED}error${Console.RESET}] " + message)
  }

  def info(message: String) {
    Console.err.println("[info] " + message)
  }
}

class Hi {
  import Hi._

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

  def batch(paths: Array[String]) {
    // Similar to code in: `scala.tools.nsc.interpreter.ILoop.pasteCommand`.
    // The `scala.tools.nsc.ScriptRunner` utility has given me some problems:
    //  * Compiling in a scope in which a root object `Hi` is missing in a
    //    former version of this code. Makes sense because it invokes an
    //    external "compile server".
    //  * Getting stuck -- not terminating/returning -- when I tried to use it
    //    at a later time. Probably not using it right but there was nothing
    //    obviously wrong about the invocation.
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
}

