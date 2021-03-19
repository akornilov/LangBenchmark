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
#include "dcffile.h"

#include <iostream>
#include <filesystem>

#define RADIUS 5000.0

bool Performer::segmentReceived(const DcfSegment& segment)
{
    if (!_pos.isValid()) { _pos = segment.getStart(); }
    if (segment.getStart().getDistance(_pos) <= RADIUS || segment.getEnd().getDistance(_pos) <= RADIUS) {
        _segments[segment.getClustId()].push_back(segment);
    }
    return true;
}

void Performer::invalidLine(const std::string& line)
{
    std::cerr << "Invalid DCF line: " << line << std::endl;
}

int Performer::start()
{
    for (const auto& file: std::filesystem::directory_iterator(_directory)) {
        if (file.path().extension() == ".dcf") {
            std::cout << "Processing '" << file << "'..." << std::endl;
            DcfFile(file.path().string()).processFile(this);
        }
    }
    std::cout << "Found " << _segments.size() << " segments;" << std::endl;
    return 0;
}
