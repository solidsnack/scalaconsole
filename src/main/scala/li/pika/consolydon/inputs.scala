package li.pika.consolydon.inputs

import java.io.File


sealed trait Base {
  def label: String
  def text: String
}


case class Scala(label: String, text: String) extends Base

object Scala {
  def apply(file: File): Scala = {
    val text: String = new reflect.io.File(file).slurp()
    Scala(file.getPath, text)
  }

  def apply(text: String): Scala = {
    Scala(s"string//${text.hashCode.toHexString}", text)
  }
}

