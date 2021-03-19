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

import java.io.*
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.concurrent.thread

fun executableNullHandler(line: String, type: Executable.OutputType) = Unit

class Executable(name: String, cache: LookupCache? = null) {
    enum class OutputType { Std, Err }

    override fun toString(): String = executable.toString()

    class Launcher internal constructor(args: List<String>, private val handler: (String, OutputType) -> Unit) {
        var workingDirectory: File
            get() = builder.directory()
            set(value) { builder.directory(value) }

        val environment: MutableMap<String, String>
            get() = builder.environment()

        fun start(): Future<Int> = Result(builder.start(), handler)

        private val builder = ProcessBuilder(args)
            .redirectInput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
    }

    fun launcher(args: List<String>, handler: (String, OutputType) -> Unit = ::executableNullHandler): Launcher = Launcher(listOf(executable.toString()) + args, handler)

    private class Result(private val process: Process, private val handler: (String, OutputType) -> Unit): Future<Int> {
        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            if (isDone) return false
            if (mayInterruptIfRunning) process.destroy()
            cancelled = true
            return true
        }

        override fun isCancelled(): Boolean = cancelled

        override fun isDone(): Boolean = cancelled || !process.isAlive

        override fun get(): Int = finish { process.waitFor() }

        override fun get(timeout: Long, unit: TimeUnit): Int = finish {
            if (process.waitFor(timeout, unit)) process.exitValue() else throw TimeoutException()
        }

        private fun listenPipe(pipe: InputStream, type: OutputType) {
            BufferedReader(InputStreamReader(pipe)).useLines {
                for (line in it) { handler(line, type) }
            }
        }

        private fun finish(wait: () -> Int): Int {
            val result = wait()
            stdListener.join()
            errListener.join()
            return result
        }

        private var cancelled = false
        private val stdListener = thread { listenPipe(process.inputStream, OutputType.Std) }
        private val errListener = thread { listenPipe(process.errorStream, OutputType.Err) }
    }

    private fun lookup(name: String): File = lookupFile(name, systemPaths) { nameVariants(it, ExecutableSuffixes, currentPlatform == Platform.Linux) }

    private val executable = cache?.get(name, ::lookup) ?: lookup(name)
}

private val ExecutableSuffixes = if (currentPlatform == Platform.Windows) listOf("exe", "bat", "kexe") else listOf("sh", "exe", "kexe")
