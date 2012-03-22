package com.github.hexx.scalamblr

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
    Scalamblr.main(config.arguments)
    Exit(0)
  }
}

object Scalamblr {
  import com.twitter.util.Eval

  val scalamblrConfigFile = scala.util.Properties.userHome + "/.scalamblr/config.scala"

  val eval = new Eval
  val config = eval[Config](new java.io.File(scalamblrConfigFile))

  case class Option(var hostname: String = config.defaultHostname,
                    var title: String = "",
                    var tags: String = "",
                    var body: String = "",
                    var markdown: Boolean = false,
                    var help: Boolean = false)

  def main(args: Array[String]) {
    import scopt._

    var option = Option()

    val parser = new OptionParser("scalamblr") {
      opt("hostname", "hostname", { v => option.hostname = v })
      opt("title", "title", { v => option.title = v })
      opt("tags", "tags", { v => option.tags = v })
      opt("markdown", "use markdown", { option.markdown = true })
      opt("h", "help", "help", { option.help = true })
      opt("body", "post body", { v => option.body = v})
    }
    if (!parser.parse(args)) {
      return
    }
    if (option.help) {
      println(parser.usage)
      return
    }

    def readBody: String = {
      import scala.io.Source

      if (option.body.length > 0) {
        return Source.fromFile(option.body).mkString
      } else {
        return Source.stdin.mkString
      }
    }

    def post(body: String) {
      import dispatch._
      import dispatch.oauth.Consumer
      import com.github.hexx.dispatch.tumblr._

      val consumer = Consumer(config.consumerKey, config.consumerSecret)
      val http = new Http
      val access_token = http(Auth.access_token(consumer, config.username, config.password))
      val query = Seq("type" -> "text",
                      "title" -> option.title,
                      "tags" -> option.tags,
                      "markdown" -> option.markdown.toString,
                      "body" -> body)
      http(Blog.post(option.hostname, consumer, access_token, query:_*))
    }

    post(readBody)
  }
}

case class Exit(val code: Int) extends xsbti.Exit
