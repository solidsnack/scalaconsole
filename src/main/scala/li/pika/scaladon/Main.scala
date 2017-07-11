package li.pika.scaladon


object Main extends App {
  try {
    val engine = Engine()

    val task = args match {
      case Array() => engine.interpreter()
      case Array("-i") => engine.interpreter()
      case Array(s) => engine.script(s)
      case Array(s, rest @ _*) => engine.script(s, rest)
    }

    task()
  } catch {
    case e: errors.Error => {
      Console.err.println(s"[${Console.RED}error${Console.RESET}] $e")
      System.exit(1)
    }
  }
}
