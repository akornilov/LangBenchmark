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

#include "dcffile.h"
#include "dcfsegment.h"

#include <fstream>
#include <sstream>
#include <vector>

void DcfFile::processFile(IEventHandler *handler)
{
    std::ifstream stream(_file);
    if (handler && stream.good()) {
        std::string line;
        while (std::getline(stream, line)) {
            if (line.empty() || line.rfind("SEG:") != 0) {
                continue;
            }
            std::stringstream ss(line);
            std::vector<std::string> parts;
            std::string part;
            while(std::getline(ss, part, ':')) {
               parts.push_back(part);
            }
            if (parts.size() < 6) {
                handler->invalidLine(line);
                continue;
            }
            try {
                const auto lat1 = std::stod(parts.at(2));
                const auto lon1 = std::stod(parts.at(1));
                const auto lat2 = std::stod(parts.at(4));
                const auto lon2 = std::stod(parts.at(3));
                auto index = -1;
                for (auto i = 5u; i < parts.size(); i++) {
                    if (parts.at(i).rfind("ClustID") == 0) {
                        index = static_cast<decltype(index)>(i);
                        break;
                    }
                }
                if (index < 0 || static_cast<unsigned>(index + 1) >= parts.size()) {
                    handler->invalidLine(line);
                    continue;
                }
                DcfSegment segment(Position(lat1, lon1), Position(lat2, lon2), parts.at(static_cast<unsigned>(index) + 1));
                if (!handler->segmentReceived(segment)) {
                    break;
                }
            } catch (...) {
                handler->invalidLine(line);
                continue;
            }
        }
    }
}
