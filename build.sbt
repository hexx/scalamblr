seq(conscriptSettings :_*)

organization := "com.github.hexx"

name := "scalamblr"

version := "0.0.1"

scalaVersion := "2.9.1"

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "2.0.1",
  "com.twitter" % "util-eval" % "1.12.13",
  "com.github.hexx" %% "dispatch-tumblr" % "0.0.1",
  "org.specs2" %% "specs2" % "1.9" % "test"
)

resolvers ++= Seq(
  "twiiter" at "http://maven.twttr.com/"
)
