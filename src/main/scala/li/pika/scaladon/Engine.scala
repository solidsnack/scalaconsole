package li.pika.scaladon

import java.io.File
import javax.script.{Bindings, SimpleBindings}

import tasks._


case class Engine(bindings: Bindings = new SimpleBindings(),
                  prompt: Option[String] = None,
                  welcome: Option[String] = None) {
  def task(input: inputs.Input): Task = RunScript(input, bindings)

  def task(path: String): Task = task(new File(path))

  def task(file: File): Task = RunScript(inputs.File(file), bindings)

  def interpreter(): Task = GoInteractive(prompt = prompt, welcome = welcome)
}
