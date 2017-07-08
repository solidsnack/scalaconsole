package li.pika.scaladon


object Main extends App {
  try {
    val engine = Engine()

    val tasks = args match {
      case Array() => Array(engine.interpreter())
      case _ => args.map(engine.task(_))
    }

    for (task <- tasks) task()
  } catch {
    case e: errors.Error => {
      Console.err.println(s"[${Console.RED}error${Console.RESET}] $e")
      System.exit(1)
    }
  }
}
