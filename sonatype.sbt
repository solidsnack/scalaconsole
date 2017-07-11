sonatypeProfileName := "li.pika"
publishMavenStyle := true
licenses := Seq(
  "APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)
homepage := Some(url("https://github.com/solidsnack/scaladon"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/solidsnack/scaladon"),
    "scm:git@github.com:solidsnack/scaladon.git"
  )
)
developers := List(
  Developer(id="solidsnack",
            name="Jason Dusek",
            email="jason.dusek@gmail.com",
            url=url("https://github.com/solidsnack"))
)