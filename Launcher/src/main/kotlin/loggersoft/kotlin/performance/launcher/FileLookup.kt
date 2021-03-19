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
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.IllegalStateException

private const val SuffixChar = '.'

val systemPaths: List<File> by lazy {
    (System.getenv("path")
        ?: System.getenv("PATH")
        ?: System.getenv("Path")
        ?: throw IOException("Environment variable 'PATH' not found")).split(File.pathSeparatorChar).map { File(it.trim()) }
}

class LookupCache(private val limit: Int = 128) {

    fun get(name: String, lookup: (String) -> File): File {
        while (cache.size >= limit) { cache.remove(cache.keys.first()) }
        return cache.getOrPut(name.trim()) { lookup(name) }
    }

    private val cache: MutableMap<String, File> = mutableMapOf()
}

fun nameVariants(name: String, suffixes: List<String>, needStripped: Boolean = false): Sequence<String> =
    Sequence { object : Iterator<String> {
            override fun hasNext(): Boolean = index + 1 <= maxIndex

            override fun next(): String {
                check(index in -1 until maxIndex) { "Next element is not present" }
                return when(++index) {
                    in suffixes.indices -> "${strip}${SuffixChar}${suffixes[index]}"
                    suffixes.size -> name.trim()
                    suffixes.size + 1 -> strip
                    else -> throw IllegalStateException("Invalid index")
                }
            }

            private val strip: String by lazy {
                val suffixIndex = name.lastIndexOf(SuffixChar)
                (if (suffixIndex < 0) name else name.substring(0, suffixIndex)).trim()
            }

            private var index: Int = -1
            private val maxIndex: Int = suffixes.size + (if (needStripped && name.trim() != strip) 1 else 0)
        }
    }

fun lookupFile(name: String, paths: List<File>, variants: (String) -> Sequence<String>): File {
    fun check(directory: File, name: String): File? = variants(name).map { File(directory, it) }.firstOrNull { it.exists() }

    return if ('/' in name || '\\' in name || File.separator in name) File(name.trim()).apply { check(exists()) }
    else paths.asSequence().map { check(it, name) }.firstOrNull { it != null } ?: throw FileNotFoundException("File not found: $name")
}
