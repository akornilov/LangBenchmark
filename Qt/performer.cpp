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

#define RADIUS 5000.0

bool Performer::segmentReceived(const DcfSegment &segment)
{
    if (!_pos.isValid()) { _pos = segment.getStart(); }
    if (segment.getStart().getDistance(_pos) <= RADIUS || segment.getEnd().getDistance(_pos) <= RADIUS) {
        _segments[segment.getClustId()] << segment;
    }
    return true;
}

void Performer::invalidLine(const QString& line)
{
    std::cerr << "Invalid DCF line: " << line.toStdString() << std::endl;
}

int Performer::start()
{
    foreach (const auto& file, _directory.entryInfoList()) {
        std::cout << "Processing '" << file.fileName().toStdString() << "'..." << std::endl;
        DcfFile(file.filePath()).processFile(this);
    }
    std::cout << "Found " << _segments.size() << " segments;" << std::endl;
    return 0;
}
