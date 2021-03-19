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

#include <QTextStream>

void DcfFile::processFile(IEventHandler *handler)
{
    if (handler && _file.open(QIODevice::ReadOnly)) {
        QTextStream stream(&_file);
        while (!stream.atEnd()) {
            const QString &line = stream.readLine().trimmed();
            if (line.isEmpty() || !line.startsWith("SEG:")) {
                continue;
            }
            const QStringList& parts = line.split(":");
            if (parts.size() < 6) {
                handler->invalidLine(line);
                continue;
            }
            auto ok = false;
            const auto lat1 = parts.at(2).toDouble(&ok);
            if (!ok) {
                handler->invalidLine(line);
                continue;
            }
            ok = false;
            const auto lon1 = parts.at(1).toDouble(&ok);
            if (!ok) {
                handler->invalidLine(line);
                continue;
            }
            ok = false;
            const auto lat2 = parts.at(4).toDouble(&ok);
            if (!ok) {
                handler->invalidLine(line);
                continue;
            }
            ok = false;
            const auto lon2 = parts.at(3).toDouble(&ok);
            if (!ok) {
                handler->invalidLine(line);
                continue;
            }
            auto index = -1;
            for (auto i = 5; i < parts.size(); i++) {
                if (parts.at(i).startsWith("ClustID")) {
                    index = i;
                    break;
                }
            }
            if (index < 0 || index + 1 >= parts.size()) {
                handler->invalidLine(line);
                continue;
            }
            DcfSegment segment(Position(lat1, lon1), Position(lat2, lon2), parts[index + 1]);
            if (!handler->segmentReceived(segment)) {
                break;
            }
        }
        _file.close();
    }
}
