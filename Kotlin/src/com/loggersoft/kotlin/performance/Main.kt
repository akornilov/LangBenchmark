package com.loggersoft.kotlin.performance

import kotlin.system.measureTimeMillis

fun main(args : Array<String>) {
    val workingTime = measureTimeMillis {
        println("Start...")
        Performer(if (args.isNotEmpty()) args[0] else null).start()
    }
    println("Finish: ${workingTime}ms")
}