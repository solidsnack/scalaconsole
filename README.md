An executable uber jar with an embedded Scala repl.

```bash
 :; sbt assembly
    ...
    [success] Total time: 14 s, completed Jun 10, 2017 5:08:05 PM

 :; target/scala-*/bin/scalaconsole.jar
    Welcome to Scala 2.12.2 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_102).
    Type in expressions for evaluation. Or try :help.

    scala> li.pika.hi.Hi.internalValue
    res0: String = This value is drawn from a class in the project.
```

Note that the Jar must be called `<something>.jar` for this to work -- if the
archive passed to Java does not end in `.jar`, some classpath issues arise.

(As long as you use a wrapper script and not a symlink, you can create an entry point that does not end in `.jar`.)
