#include "position.h"

#include <QtMath>
#include <math.h>

Position::Position() :
    mLat(1000.0),
    mLon(1000.0)
{
}

Position::Position(double lat, double lon) :
    mLat(lat),
    mLon(lon)
{
}

Position::Position(const Position &other) :
    mLat(other.mLat),
    mLon(other.mLon)
{
}

Position::~Position()
{
}

Position &Position::operator=(const Position &other)
{
    if (&other == this) {
        return *this;
    }
    mLat = other.mLat;
    mLon = other.mLon;
    return *this;
}

double Position::getDistance(const Position &other) const
{
    const double lat1 = qDegreesToRadians(mLat);
    const double lon1 = qDegreesToRadians(mLon);
    const double lat2 = qDegreesToRadians(other.mLat);
    const double lon2 = qDegreesToRadians(other.mLon);
    const double sinLat = sin((lat2 - lat1) / 2.0);
    const double sinLon = sin((lon2 - lon1) / 2.0);
    const double a = sinLat * sinLat + cos(lat1) * cos(lat2) * sinLon * sinLon;
    return 2.0 * atan2(sqrt(a), sqrt(1.0 - a));
}
