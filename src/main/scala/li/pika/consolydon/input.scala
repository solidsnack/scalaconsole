package li.pika.consolydon.input

import java.io.File


sealed trait InputType {
  def label: String
  def text: String
}


sealed trait ScalaInput extends InputType


case class ScalaFile(file: File) extends ScalaInput {
  val label = file.getPath

  lazy val text: String = new reflect.io.File(file).slurp()
}

object ScalaFile {
  def apply(path: String): ScalaFile = ScalaFile(new File(path))
}


case class ScalaText(text: String, label: String) extends ScalaInput

object ScalaText {
  def apply(text: String): ScalaText = {
    ScalaText(text, s"string//${text.hashCode.toHexString}")
  }
}
