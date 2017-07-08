name := "scaladon"
organization := "li.pika"

enablePlugins(GitBranchPrompt)
version := git.gitDescribedVersion.value.getOrElse("0.0.0")


scalaVersion := "2.12.2"

crossScalaVersions := Seq("2.11.11", "2.12.2", "2.13.0-M1")


assemblyJarName in assembly := s"uber.jar"


libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value
)