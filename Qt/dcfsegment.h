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

#ifndef DCFSEGMENT_H
#define DCFSEGMENT_H

#include "position.h"

#include <QString>

class DcfSegment final
{
public:
    DcfSegment(const Position& start, const Position& end, const QString& clustId):
        _start(start),
        _end(end),
        _clustId(clustId)
    {
        updateLength();
    }

    Position getStart() const {
        return _start;
    }

    Position getEnd() const {
        return _end;
    }

    QString getClustId() const {
        return _clustId;
    }

    double getLength() const {
        return _length;
    }

    void setStart(const Position& start) {
        _start = start;
        updateLength();
    }

    void setEnd(const Position& end) {
        _end = end;
        updateLength();
    }

    void setClustId(const QString& clustId) {
        _clustId = clustId;
    }

private:
    void updateLength() {
        _length = _start.getDistance(_end);
    }

    Position _start;
    Position _end;
    QString _clustId;
    double _length;
};

#endif // DCFSEGMENT_H
