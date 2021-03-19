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

interface Player {
    interface Launcher {
        val isAvailable: Boolean
        fun launch(workingDirectory: File, sourceDirectory: File, handler: (String, Executable.OutputType) -> Unit): Int
    }

    data class Result(val isValid: Boolean = false, val segments: Int = -1, val time: Int = -1)

    val name: String
    val isAvailable: Boolean

    fun launch(sourceDirectory: File): Result

    fun iterations(sourceDirectory: File, count: Int): Result {
        var time = 0
        var segments = -1
        repeat(count) {
            val result = launch(sourceDirectory)
            if (!result.isValid) return Result()
            if (segments < 0) segments = result.segments else if (segments != result.segments) return Result()
            time += result.time
        }
        return Result(true, segments, time / count)
    }
}

fun availablePlayers(): List<Player> = mutableListOf<Player>().apply {
    for (cls in playersCls) {
        val instance = cls.declaredConstructors.first().newInstance() as Player
        if (instance.isAvailable) this += instance
    }
}

private val playersCls: List<Class<out Player>> = listOf(
    PlayerJava::class.java,
    PlayerScala::class.java,
    PlayerKotlin::class.java,
    PlayerDotNet::class.java,
    PlayerCpp::class.java,
    PlayerQt::class.java,
    PlayerD::class.java,
    PlayerKotlinNative::class.java
)

private open class PlayerImpl(final override val name: String, private val launcher: Player.Launcher, private val playerDir: File = File("../$name"), private val enabled: Boolean = true): Player {
    override val isAvailable: Boolean
        get() = enabled && playerDir.exists() && launcher.isAvailable

    override fun launch(sourceDirectory: File): Player.Result = parseResult(launcher, playerDir, sourceDirectory)

    private fun parseResult(launcher: Player.Launcher, workingDirectory: File, sourceDirectory: File): Player.Result {
        var isValid = true
        var segments = -1
        var time = -1
        isValid = launcher.launch(workingDirectory, sourceDirectory) { line, type ->
            if (isValid) {
                if (type == Executable.OutputType.Std) {
                    var value = regexpMeasureTime.getFirstGroup(line)
                    if (value != null) {
                        try { time = value.toInt() } catch (e: NumberFormatException) { isValid = false }
                    }
                    value = regexpSegmentsFound.getFirstGroup(line)
                    if (value != null) {
                        try { segments = value.toInt() } catch (e: NumberFormatException) { isValid = false }
                    }
                } else {
                    isValid = false
                }
            }
        } == 0
        if (segments <= 0 || time < 0) isValid = false
        return Player.Result(isValid, segments, time)
    }

    private fun Regex.getFirstGroup(input: CharSequence): String? {
        val result = matchEntire(input) ?: return null
        val values = result.groupValues
        return if (values.size > 1) values[1] else null
    }

    private val regexpMeasureTime = Regex("""^\s*Finish:\s+(\d+)(?:[,.]\d+)?\s*ms\s*$""")
    private val regexpSegmentsFound = Regex("""^\s*Found\s+(\d+)\s+segments.*$""")
}

private open class PlayerLauncherSimple(executableName: String, cache: LookupCache? = null, private val args: (File) -> List<String>): Player.Launcher {
    private val executable: Executable? = try { Executable(executableName, cache) } catch (_: Exception) { null }

    override val isAvailable: Boolean = executable != null

    override fun launch(workingDirectory: File, sourceDirectory: File, handler: (String, Executable.OutputType) -> Unit): Int {
        check(executable != null) { "The '${executable}' is not available" }
        return executable.launcher(args(sourceDirectory), handler).apply { this.workingDirectory = workingDirectory }.start().get()
    }
}

private class PlayerLauncherKotlinNative: Player.Launcher {
    private val executable: Executable? = try { Executable("gradle") } catch (_: Exception) { null }

    override val isAvailable: Boolean = executable != null

    override fun launch(workingDirectory: File, sourceDirectory: File, handler: (String, Executable.OutputType) -> Unit): Int {
        check(executable != null) { "The Gradle is not available" }
        check(executable.launcher(listOf("assemble")).apply { this.workingDirectory = workingDirectory }.start().get() == 0) { "Failed to assemble KotlinNative" }
        return Executable(File(workingDirectory, "build/bin/native/releaseExecutable/KotlinNative.kexe").path)
                .launcher(listOf(sourceDirectory.toString()), handler).apply { this.workingDirectory = workingDirectory }.start().get()
    }
}

private class PlayerLauncherCMake(private val binaryName: String): Player.Launcher {
    private val executable: Executable? = try { Executable("cmake") } catch (_: Exception) { null }

    override val isAvailable: Boolean = executable != null

    override fun launch(workingDirectory: File, sourceDirectory: File, handler: (String, Executable.OutputType) -> Unit): Int {
        check(executable != null) { "The CMake is not available" }
        val buildDir = File("../build-$binaryName-release")
        buildDir.mkdirs()
        val configArgs = if (currentPlatform == Platform.Windows) listOf(workingDirectory.toString()) else listOf("-DCMAKE_BUILD_TYPE=Release", workingDirectory.toString())
        val buildArgs = if (currentPlatform == Platform.Windows) listOf("--build", ".", "--config", "Release") else listOf("--build", ".")
        check(executable.launcher(configArgs).apply { this.workingDirectory = buildDir }.start().get() == 0) { "Failed to configure project by CMake" }
        check(executable.launcher(buildArgs).apply { this.workingDirectory = buildDir }.start().get() == 0) { "Failed to build project by CMake" }
        val appName = (if (currentPlatform == Platform.Windows) File(File(buildDir, "Release"), "$binaryName.exe") else File(buildDir, binaryName)).path
        return Executable(appName).launcher(listOf(sourceDirectory.toString()), handler).apply { this.workingDirectory = workingDirectory }.start().get()
    }
}

private class PlayerLauncherGradle(runTask: String = "run", cache: LookupCache? = null): PlayerLauncherSimple("gradle", cache, { listOf(runTask, "--args=${it}")})

private open class PlayerGradle(name: String, runTask: String = "run"): PlayerImpl(name, PlayerLauncherGradle(runTask))
private class PlayerJava: PlayerGradle("Java")
private class PlayerScala: PlayerGradle("Scala")
private class PlayerKotlin: PlayerGradle("Kotlin")
private class PlayerD: PlayerImpl("D", PlayerLauncherSimple("dub") { listOf("run", "-b", "release", "--", it.toString()) })
private class PlayerDotNet: PlayerImpl("DotNet", PlayerLauncherSimple("dotnet") { listOf("run", "-c", "Release", "--", it.toString()) }, File("../CSharp"))
private class PlayerKotlinNative: PlayerImpl("KotlinNative", PlayerLauncherKotlinNative(), enabled = currentPlatform != Platform.Windows)
private class PlayerQt: PlayerImpl("Qt", PlayerLauncherCMake("qtperf"))
private class PlayerCpp: PlayerGradle("C++", "runRelease")
