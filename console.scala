import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.Settings


object Hi {
  def main(args: Array[String]) {
    interpreter()
  }

  val internalValue = "This value is drawn from a class in the project."

  def interpreter() {
    val settings = new Settings
    settings.deprecation.value = true
    settings.feature.value = true
    settings.Yreplsync.value = true
    settings.usejavacp.value = true
    val scala = new ILoop
    scala.process(settings)
  }
}
