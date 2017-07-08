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

  def task(path: String): Task = task(new File(path))

  def task(file: File): Task = RunScript(file, scriptSettings)

  def interpreter(): Task = RunInteractive(prompt = prompt,
                                           welcome = welcome,
                                           settings = interactiveSettings)
}
