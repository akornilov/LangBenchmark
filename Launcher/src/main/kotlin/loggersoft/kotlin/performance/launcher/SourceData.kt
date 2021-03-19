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

val DEFAULT_SOURCE_DATA_LOCATION: File = File("../Data")

class SourceData(val location: File = DEFAULT_SOURCE_DATA_LOCATION, val iterations: Int = 20, val cache: LookupCache? = LookupCache()) {
    val dataSets: List<DataSet> by lazy {
        mutableListOf<DataSet>().apply {
            for (file in location.listFiles { _, filename -> filename.trim().toLowerCase().endsWith(".7z") } ?: throw FileNotFoundException()) {
                this += DataSet(file.nameWithoutExtension, iterations, file, bin7z)
            }
        }
    }

    private val bin7z = Executable("7z", cache)
}