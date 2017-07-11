package li.pika.scaladon

import java.io.File

import tasks._


case class Engine(prompt: Option[String] = None,
                  welcome: Option[String] = None,
                  interactiveMessageHandler: ((String) => Unit)
                                             = Task.defaultWriter,
                  scriptMessageHandler: ((String) => Unit)
                                        = Task.defaultWriter) {
  val scriptSettings = Task.settings(scriptMessageHandler)
  val interactiveSettings = Task.settings(interactiveMessageHandler)

  def script(path: String, arguments: Seq[String] = Seq()): Task = {
    script(new File(path), arguments)
  }

  def script(file: File, arguments: Seq[String]): Task = {
    RunScript(file, arguments, scriptSettings)
  }

  def interpreter(): Task = RunInteractive(prompt = prompt,
                                           welcome = welcome,
                                           settings = interactiveSettings)
}
