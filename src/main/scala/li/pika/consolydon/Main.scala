package li.pika.consolydon


object Main extends App {
  val engine = Engine()
  try engine.processArguments(args) catch {
    case e: errors.Base => {
      Console.err.println(s"[${Console.RED}error${Console.RESET}] $e")
      System.exit(1)
    }
  }
}
