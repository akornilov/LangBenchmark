package com.loggersoft.dcf.converter

fun main(args : Array<String>) {
    if (args.isEmpty()) {
        println("Usage is: mp2dcf <input directory with .mp (Polish map format)> [output directory]")
        println("Copyright by Alexander Kornilov 2017")
        return
    }
    Converter(args[0], if (args.size > 1) args[1] else null).start()
}
