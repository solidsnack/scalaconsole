name := "consolydon"
organization := "li.pika"

enablePlugins(GitBranchPrompt)
version := git.gitDescribedVersion.value.getOrElse("0.0.0")


scalaVersion := "2.12.2"


val script = Seq("#!/bin/sh", "set -eu", """exec java -jar "$0" "$@"""", "")

assemblyOption in assembly := (assemblyOption in assembly).value
    .copy(prependShellScript = Some(script))

assemblyJarName in assembly := s"bin/${name.value}.jar"


libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value
)