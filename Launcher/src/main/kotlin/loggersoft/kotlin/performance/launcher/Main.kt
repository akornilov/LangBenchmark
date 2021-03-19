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

fun main() {
    try {
        val sourceData = SourceData(iterations = 1)
        if (sourceData.dataSets.isEmpty()) {
            println("Directory '${sourceData.location}' doesn't contains any data sets (*.7z).")
            return
        }
        println("Found ${sourceData.dataSets.size} data set(s) with ${sourceData.iterations} iterations:")
        for (dataSet in sourceData.dataSets) {
            println(" - processing data set '${dataSet.name}'...")
            dataSet.start()
        }
    } catch (e: Exception) {
        println("Error: ${e.localizedMessage}.")
    }
}