package li.pika.scaladon

import java.io.IOException
import java.lang.reflect.InvocationTargetException

import scala.tools.nsc.io.File
import scala.tools.nsc.{GenericRunnerSettings, ObjectRunner, ScriptRunner}


/**
  * Reimplement some of the private workings of [[ScriptRunner]], to be able
  * to see exceptions thrown by the script.
  */
class EncapsulationViolationScriptRunner {
  val runner = new ScriptRunner

  def runScript(settings: GenericRunnerSettings,
                scriptFile: String,
                scriptArgs: List[String]): Boolean = {
    if (File(scriptFile).isFile) {
      try withCompiledScript(settings, scriptFile) {
        runCompiled(settings, _, scriptArgs).left.map(throw _).right.get
      } catch {
        case e: InvocationTargetException => throw e.getCause
      }
    } else {
      throw new IOException("no such file: " + scriptFile)
    }
  }

  private def withCompiledScript(settings: GenericRunnerSettings,
                                 scriptFile: String)
                                (handler: String => Boolean): Boolean = {
    val method = runner.getClass.getDeclaredMethod(
      "withCompiledScript",
      classOf[GenericRunnerSettings],
      classOf[String],
      classOf[(String) => Boolean]
    )
    method.setAccessible(true)
    method.invoke(runner, settings, scriptFile, handler).asInstanceOf[Boolean]
  }

  private def runCompiled(settings: GenericRunnerSettings,
                          compiledLocation: String,
                          argv: List[String]): Either[Throwable, Boolean] = {
    val cp = File(compiledLocation).toURL +: settings.classpathURLs
    ObjectRunner.runAndCatch(cp, runner.scriptMain(settings), argv)
  }
}
