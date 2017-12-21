#ifndef IEVENTHANDLER_H
#define IEVENTHANDLER_H

class QString;
class DcfSegment;

class IEventHandler
{
public:
    virtual bool segmentReceived(const DcfSegment &segment) = 0;
    virtual void invalidLine(const QString &line) = 0;
};

#endif // IEVENTHANDLER_H
