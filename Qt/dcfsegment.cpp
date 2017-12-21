#include "dcfsegment.h"

DcfSegment::DcfSegment()
{
}

DcfSegment::DcfSegment(const Position &start, const Position &end, const QString &clustId) :
    mStart(start),
    mEnd(end),
    mClustId(clustId)
{
    updateLength();
}

DcfSegment::DcfSegment(const DcfSegment &other) :
    mStart(other.mStart),
    mEnd(other.mEnd),
    mClustId(other.mClustId),
    mLength(other.mLength)
{
}

DcfSegment::~DcfSegment()
{
}

DcfSegment &DcfSegment::operator=(const DcfSegment &other)
{
    if (&other == this) {
        return *this;
    }
    mStart = other.mStart;
    mEnd = other.mEnd;
    mClustId = other.mClustId;
    mLength = other.mLength;
    return *this;
}

void DcfSegment::setStart(const Position &start)
{
    mStart = start;
    updateLength();
}

void DcfSegment::setEnd(const Position &end)
{
    mEnd = end;
    updateLength();
}

void DcfSegment::setClustId(const QString &clustId)
{
    mClustId = clustId;
}

void DcfSegment::updateLength()
{
    mLength = mStart.getDistance(mEnd);
}
