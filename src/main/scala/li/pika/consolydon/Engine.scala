package li.pika.consolydon

import javax.script.{Bindings, SimpleBindings}
import scala.util.matching.Regex

import input._


case class Engine(bindings: Bindings = new SimpleBindings())
  extends ((InputType) => Run) {
  def apply(input: InputType): Run = input match {
    case input: ScalaInput => Scala(input, bindings)
  }

  def mapPaths(paths: String*): Seq[Either[String, Run]] = {
    val scala: Regex = ".+[.](sc|scala)$".r
    val js: Regex = ".+[.]js$".r
    for (path <- paths) yield path match {
      case scala(_*) => Right(this(ScalaFile(path)))
      case js(_*) => Left(path)
      case _ => Left(path)
    }
  }
}

