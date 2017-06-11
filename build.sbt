import sbtassembly.AssemblyPlugin.defaultShellScript


name := "scalaconsole"

version := "1.0"

scalaVersion := "2.12.2"


assemblyOption in assembly := (assemblyOption in assembly).value
    .copy(prependShellScript = Some(defaultShellScript))

assemblyJarName in assembly := s"bin/${name.value}.jar"


libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value
)