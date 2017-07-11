import java.nio.file._
import scala.util.Random

object Main extends App {
  val token = Random.nextLong().toHexString
  var path = Paths.get(args(0))
  Console.err.println(s"Writing '${token}' to: ${path}")
  Files.createFile(path)
  Files.write(path, s"$token\n".getBytes())
}
