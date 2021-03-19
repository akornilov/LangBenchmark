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

#ifndef POSITION_H
#define POSITION_H

#include <QtMath>

#include <cmath>

class Position final
{
public:
    Position(double lat, double lon):
        _lat(lat),
        _lon(lon)
    {
    }

    bool isValid() const {
        return _lat >= -90.0 && _lat <= 90.0 && _lon >= -180.0 && _lon <= 180.0;
    }

    double getLat() const {
        return _lat;
    }

    double getLon() const {
        return _lon;
    }

    void setLat(double lat) {
        _lat = lat;
    }

    void setLon(double lon) {
        _lon = lon;
    }

    double getDistance(const Position &other) const {
        const auto lat1 = qDegreesToRadians(_lat);
        const auto lon1 = qDegreesToRadians(_lon);
        const auto lat2 = qDegreesToRadians(other._lat);
        const auto lon2 = qDegreesToRadians(other._lon);
        const auto sinLat = sin((lat2 - lat1) / 2.0);
        const auto sinLon = sin((lon2 - lon1) / 2.0);
        const auto a = sinLat * sinLat + cos(lat1) * cos(lat2) * sinLon * sinLon;
        return 2.0 * atan2(sqrt(a), sqrt(1.0 - a));
    }

private:
    double _lat;
    double _lon;
};

#endif // POSITION_H
