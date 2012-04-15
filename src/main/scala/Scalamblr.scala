package com.github.hexx.scalamblr

import scala.io.Source
import com.twitter.util.Eval
import dispatch._
import dispatch.oauth.Consumer
import scopt._
import com.github.hexx.dispatch.tumblr._

trait Config {
  def defaultHostname: String
  def markdown = false
  def consumerKey: String
  def consumerSecret: String
  def username: String
  def password: String
}

class Scalamblr extends xsbti.AppMain {
  def run(config: xsbti.AppConfiguration) = {
    Exit(Scalamblr.run(config.arguments))
  }
}

object Scalamblr {
  val scalamblrConfigFile = scala.util.Properties.userHome + "/.scalamblr/config.scala"
  val eval = new Eval
  val config = eval[Config](new java.io.File(scalamblrConfigFile))

  case class CommandLineOption(
      var hostname: String = config.defaultHostname,
      var title: String = "",
      var tags: String = "",
      var body: String = "",
      var markdown: Boolean = false,
      var help: Boolean = false)

  def run(args: Array[String]): Int = {
    var option = CommandLineOption()
    val parser = new OptionParser("scalamblr") {
      opt("hostname", "hostname", { v => option.hostname = v })
      opt("title", "title", { v => option.title = v })
      opt("tags", "tags", { v => option.tags = v })
      opt("markdown", "use markdown", { option.markdown = true })
      opt("h", "help", "help", { option.help = true })
      opt("body", "body filename", { v => option.body = v})
    }
    if (!parser.parse(args)) {
      return 0
    }
    if (option.help) {
      println(parser.usage)
      return 0
    }

    def readBody = {
      if (option.body.length > 0) {
        Source.fromFile(option.body).mkString
      } else {
        Source.stdin.mkString
      }
    }

    def post(body: String) {
      val consumer = Consumer(config.consumerKey, config.consumerSecret)
      val http = new Http
      val access_token = http(Auth.access_token(consumer, config.username, config.password))
      val query = Seq(
        "type" -> "text",
        "title" -> option.title,
        "tags" -> option.tags,
        "markdown" -> option.markdown.toString,
        "body" -> body)
      http(Blog.post(option.hostname, consumer, access_token, query:_*))
    }

    post(readBody)
    0
  }

  def main(args: Array[String]) {
    System.exit(run(args))
  }
}

case class Exit(val code: Int) extends xsbti.Exit
