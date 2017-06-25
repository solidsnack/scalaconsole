An executable uber jar with an embedded Scala repl.

```bash
 :; sbt assembly
    ...
    [success] Total time: 15 s, completed Jun 25, 2017 2:28:04 PM

 :; target/scala-*/bin/consolydon.jar
    Welcome to Scala 2.12.2 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_102).
    Type in expressions for evaluation. Or try :help.

    scala> li.pika.consolydon.Scala.defaultSettings()
    res0: scala.tools.nsc.Settings =
    Settings {
      -d = .
      -deprecation = true
      -usejavacp = true
      -feature = true
    }
```

Note that the Jar must be called `<something>.jar` for this to work -- if the
archive passed to Java does not end in `.jar`, classpath issues arise.

(As long as you use a wrapper script and not a symlink, you can create an entry
point that does not end in `.jar`.)
