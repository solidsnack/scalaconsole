package li.pika.consolydon

import java.io.FileReader
import javax.script._


class JavaScript {
  private val factory = new ScriptEngineManager
  private val engine: ScriptEngine = factory.getEngineByName("JavaScript")

  def batch(paths: Array[String]) {
    for (path <- paths) {
      val f = new FileReader(path)
      val code = engine.asInstanceOf[Compilable].compile(f)
      code.eval()
    }
  }
}
