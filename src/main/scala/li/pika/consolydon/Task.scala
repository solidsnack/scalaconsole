package li.pika.consolydon

import javax.script.Bindings


/**
  * As [[Task]] is a "ready to be run" combination of source code input,
  * variable bindings and an interpreter or compiler. Calling a task (or using
  * `.run()`) initiates interpretation or compilation and execution.
  */
trait Task extends (() => Unit) with Runnable {
  def source: inputs.Base
  def bindings: Bindings
  def run() { apply() }
}
