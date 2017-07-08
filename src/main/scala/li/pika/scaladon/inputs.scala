package li.pika.scaladon.inputs


sealed trait Input {
  def label: String
  def text: String
}


case class Text(text: String, label: String) extends Input

object Text {
  def apply(text: String): Text = {
    Text(s"string//${text.hashCode.toHexString}", text)
  }
}


case class File(file: java.io.File) extends Input {
  val label: String = file.getPath
  val text: String = new reflect.io.File(file).slurp()
}

object File {
  def apply(path: String): File = File(new java.io.File(path))
}

