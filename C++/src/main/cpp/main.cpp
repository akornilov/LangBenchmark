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

#include "performer.h"

#include <iostream>
#include <chrono>

int main(int argc, char *argv[])
{
    auto const startTime = std::chrono::system_clock::now();
    std::cout << "Start..." << std::endl;
    const auto result = Performer(argc > 1 ? argv[1] : ".").start();
    std::cout << "Finish: " << std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now() - startTime).count() << "ms" << std::endl;
    return result;
}
