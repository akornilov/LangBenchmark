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

#ifndef PERFORMER_H
#define PERFORMER_H

#include "ieventhandler.h"
#include "position.h"
#include "dcfsegment.h"

#include <QDir>
#include <QHash>
#include <QList>

#include <limits>

class QString;

class Performer : public IEventHandler
{
public:
    explicit Performer(const QString& directory):
        _directory(directory),
        _pos(std::numeric_limits<double>::quiet_NaN(), std::numeric_limits<double>::quiet_NaN())
    {
        QStringList filters;
        filters << "*.dcf";
        _directory.setNameFilters(filters);
        _directory.setFilter(QDir::Files);
    }

    bool segmentReceived(const DcfSegment& segment) override;
    void invalidLine(const QString& line) override;

    int start();

private:
    QDir _directory;
    QHash<QString, QList<DcfSegment>> _segments;
    Position _pos;
};

#endif // PERFORMER_H
