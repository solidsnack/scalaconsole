package li.pika.consolydon

import javax.script.Bindings


trait Run extends (() => Unit) {
  def source: input.InputType
  def bindings: Bindings
}
