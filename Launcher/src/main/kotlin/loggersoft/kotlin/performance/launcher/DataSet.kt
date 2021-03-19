/*
 * Copyright (C) 2021 Alexander Kornilov (akornilov.82@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package loggersoft.kotlin.performance.launcher

import java.io.File
import java.io.IOException

class DataSet internal constructor(val name: String, val iterations: Int, private val archive: File, private val bin7z: Executable) {

    fun start() {
        cleanup()
        extract()
        val players = availablePlayers()
        check(players.isNotEmpty()) { "Players are not available"}
        players.first().launch(workingDirectory)
        val report = mutableListOf<ReportItem>()
        var segments = -1
        for(player in players) {
           print("    * ${player.name}... ")
           val result = player.iterations(workingDirectory, iterations)
           if (segments < 0) segments = result.segments
           if (!result.isValid || result.segments != segments) {
                println("[FAILED]")
                continue
           }
           println("[OK]")
           report += ReportItem(player.name, result.time)
        }
        report.sort()
        println()
        println("Results for $currentPlatform:")
        for ((index, i) in report.withIndex()) {
            println("  ${index + 1}. ${i.name}: ${i.time}ms, ${(i.time.toDouble() / report.first().time.toDouble()).format(2)}")
        }
        cleanup()
    }

    private fun cleanup() {
        for (file in workingDirectory.listFiles { _, filename -> filename.trim().toLowerCase().endsWith(".dcf") } ?: throw IOException()) {
            file.delete()
        }
    }

    private fun extract() {
        check(bin7z.launcher(listOf("x", archive.name)).apply { workingDirectory = this@DataSet.workingDirectory }.start().get() == 0)
    }

    private data class ReportItem(val name: String, val time: Int) : Comparable<ReportItem> {
        override fun compareTo(other: ReportItem): Int = time.compareTo(other.time)
    }

    private fun Double.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)

    private val workingDirectory = archive.parentFile
}
