package com.loggersoft.scala.performance

object Main {
  def main(args: Array[String]) = {
    var startTime = System.currentTimeMillis()
    println("Start...")
    startTime = System.currentTimeMillis()
    new Performer(if (!args.isEmpty) args(0) else null).start()
    println(s"Finish: ${System.currentTimeMillis() - startTime}ms")
  }
}
