package loggersoft.kotlin.performance

import kotlin.system.measureTimeMillis

fun main(args : Array<String>) {
    println("Start...")
    val workingTime = measureTimeMillis {
        Performer(if (args.isNotEmpty()) args[0] else null).start()
    }
    println("Finish: ${workingTime}ms")
}