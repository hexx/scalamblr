package com.github.hexx.scalamblr

class Scalamblr extends xsbti.AppMain {
  def run(config: xsbti.AppConfiguration) = {
    Exit(Scalamblr.run(config.arguments))
  }
}

object Scalamblr {
  def run(args: Array[String]): Int = {
    println("Hello World: " + args.mkString(" "))
    0
  }
  def main(args: Array[String]) {
    System.exit(run(args))
  }
}

case class Exit(val code: Int) extends xsbti.Exit
