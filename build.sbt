name := "scaladon"
organization := "li.pika"

enablePlugins(GitBranchPrompt)
version := git.gitDescribedVersion.value.getOrElse("0.0.0")
                                  .split("-").take(2).mkString(".")

scalaVersion := "2.12.2"

crossScalaVersions := Seq("2.11.11", "2.12.2", "2.13.0-M1")


assemblyJarName in assembly := s"uber.jar"


libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value
)


// Settings for publication to Sonatype.
publishTo := Some({
  import Opts.resolver._
  if (isSnapshot.value) sonatypeSnapshots else sonatypeStaging
})
