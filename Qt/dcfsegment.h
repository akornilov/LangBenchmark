#ifndef DCFSEGMENT_H
#define DCFSEGMENT_H

#include "position.h"
#include "QString"

class DcfSegment
{
public:
    explicit DcfSegment();
    explicit DcfSegment(const Position &start, const Position &end, const QString &clustId);
    DcfSegment(const DcfSegment& other);
    virtual ~DcfSegment();

    DcfSegment& operator= (const DcfSegment& other);

    Position getStart() const {
        return mStart;
    }

    Position getEnd() const {
        return mEnd;
    }

    QString getClustId() const {
        return mClustId;
    }

    double getLength() const {
        return mLength;
    }

    void setStart(const Position &start);
    void setEnd(const Position &end);
    void setClustId(const QString &clustId);

private:
    void updateLength();

    Position mStart;
    Position mEnd;
    QString mClustId;
    double mLength;
};

#endif // DCFSEGMENT_H
