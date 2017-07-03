package li.pika.consolydon

import java.io.{File, IOException}
import java.nio.file.Path
import javax.script.{Bindings, SimpleBindings}
import scala.collection.mutable
import scala.util.matching.Regex

import errors._


/** An engine serves as a [[Task]] factory, and can also run [[Task]]s. An
  * engine accepts inputs.
  *
  * An input can be a string serving as a path name, a [[Path]] or a [[File]].
  *
  * @param bindings
  */
case class Engine(bindings: Bindings = new SimpleBindings()) {
  import Engine._

  @throws[Base]
  @throws[IllegalArgumentException]
  @throws[IOException]
  @throws[NullPointerException]
  @throws[SecurityException]
  def processArguments(args: Array[String]) {
    var declaredNextAs: Option[Class[_ <: Task]] = None
    val mapped = new mutable.ArrayBuffer[Task](args.length)

    for (arg <- args) arg match {
      case "--js" => throw NoSuchEngineErr("js")
      case "--scala" => declaredNextAs = Some(classOf[Scala])
      case _ => mapped += {
        declaredNextAs match {
          case None => task(arg)
          case Some(cls) => {
            declaredNextAs = None
            task(arg, cls)
          }
        }
      }
    }

    for (task <- mapped) task()
  }

  def task(input: inputs.Base): Task = input match {
    case input: inputs.Scala => Scala(input, bindings)
  }

  @throws[Base]
  @throws[IllegalArgumentException]
  @throws[IOException]
  @throws[NullPointerException]
  @throws[SecurityException]
  def task(path: String): Task = task(new File(path))

  @throws[Base]
  @throws[IllegalArgumentException]
  @throws[IOException]
  @throws[NullPointerException]
  @throws[SecurityException]
  def task(path: String, as: Class[_ <: Task]): Task = task(new File(path), as)

  @throws[IllegalArgumentException]
  @throws[IOException]
  @throws[NullPointerException]
  @throws[SecurityException]
  def task(file: File): Task = file.getName match {
    case scalaRE(_*) => task(inputs.Scala(file))
    case jsRE(_*) => throw NoSuchEngineErr("js", file.getName)
    case _ => throw FileMatchErr(file.getName, scalaRE, jsRE)
  }

  @throws[Base]
  @throws[IllegalArgumentException]
  @throws[IOException]
  @throws[NullPointerException]
  @throws[SecurityException]
  def task(file: File, as: Class[_ <: Task]): Task = as match {
    case _ if as == classOf[Scala] => Scala(inputs.Scala(file), bindings)
    case _ => throw NoSuchEngineErr(as.getSimpleName, file.getName)
  }

  @throws[Base]
  @throws[IllegalArgumentException]
  @throws[IOException]
  @throws[NullPointerException]
  @throws[SecurityException]
  def task(path: Path): Task = task(path.toString)

  @throws[Base]
  @throws[IllegalArgumentException]
  @throws[IOException]
  @throws[NullPointerException]
  @throws[SecurityException]
  def task(path: Path, as: Class[_ <: Task]): Task = task(path.toString, as)
}

object Engine {
  val scalaRE: Regex = ".+[.](sc|scala)$".r
  val jsRE: Regex = ".+[.]js$".r
}
