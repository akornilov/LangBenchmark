#include "performer.h"
#include "dcffile.h"
#include "dcfsegment.h"

#include <QDebug>

#define RADIUS 5000.0

Performer::Performer(const QString &directory) :
    mDirectory(directory)
{
    qRegisterMetaType<DcfSegment>("DcfSegment");
    QStringList filters;
    filters << "*.dcf";
    mDirectory.setNameFilters(filters);
    mDirectory.setFilter(QDir::Files);
}

Performer::~Performer()
{
}

bool Performer::segmentReceived(const DcfSegment &segment)
{
    if (mPos.isValid()) {
        mPos = segment.getStart();
    }
    if (segment.getStart().getDistance(mPos) <= RADIUS || segment.getEnd().getDistance(mPos) <= RADIUS) {
        mSegments[segment.getClustId()] << segment;
    }
    return true;
}

void Performer::invalidLine(const QString &line)
{
    qWarning() << "Invalid DCF line:" << line;
}

int Performer::start()
{
    foreach (const QFileInfo &file, mDirectory.entryInfoList()) {
        qDebug() << "Processing" << file.fileName() << "...";
        DcfFile dcf(file.filePath());
        dcf.processFile(this);
    }
    qDebug() << "Found" << mSegments.size() << "segments;";
    return 0;
}
