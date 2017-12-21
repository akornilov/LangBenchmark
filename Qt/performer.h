#ifndef PERFORMER_H
#define PERFORMER_H

#include "ieventhandler.h"
#include "position.h"

#include <QDir>
#include <QHash>
#include <QList>

class QString;

class Performer : public IEventHandler
{
public:
    explicit Performer(const QString &directory);
    virtual ~Performer();

    bool segmentReceived(const DcfSegment &segment) override;
    void invalidLine(const QString &line) override;

    int start();

private:
    QDir mDirectory;
    QHash<QString, QList<DcfSegment> > mSegments;
    Position mPos;
};

#endif // PERFORMER_H
