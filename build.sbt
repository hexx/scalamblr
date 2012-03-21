seq(conscriptSettings :_*)

organization := "com.github.hexx"

name := "Tumblr Comand-line Client"

version := "0.0.1"

scalaVersion := "2.9.1"

scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "2.0.0",
  "com.twitter" % "util-eval" % "1.12.13",
  "com.github.hexx" %% "dispatch-tumblr" % "0.0.1",
  "org.specs2" %% "specs2" % "1.8.2" % "test"
)

resolvers ++= Seq(
  "twiiter" at "http://maven.twttr.com/"
)
