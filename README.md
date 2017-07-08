# `scaladon`

Embed a Scala interpreter in your application with `scaladon`.
See `li.pika.scaladon.Main` for an example of how to use the
`li.pika.scaladon.Engine`.

```bash
 :; sbt assembly
    ...
    [success] Total time: 12 s, completed Jul 7, 2017 8:03:11 PM

 :; java -jar target/scala-*/uber.jar
    Welcome to Scala 2.12.2 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_102).
    Type in expressions for evaluation. Or try :help.

    scala> val task = li.pika.scaladon.tasks.Task.settings()
    task: scala.tools.nsc.Settings =
    Settings {
      -nc = true
      -d = .
      -deprecation = true
      -usejavacp = true
      -feature = true
    }
```

# Configuring The Interpreter

The `li.pika.scaladon.Engine` allows one to set three settings:

```scala
case class Engine(bindings: Bindings = new SimpleBindings(),
                  prompt: Option[String] = None,
                  welcome: Option[String] = None) { ... }
```

The `bindings` apply for scripts while the `prompt` and `welcome` apply to the
interactive interpreter. (At present, `scaladon` is not able to set bindings
for the interactive interpreter.)
