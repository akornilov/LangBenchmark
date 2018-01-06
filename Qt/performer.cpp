#include "performer.h"
#include "dcffile.h"
#include "dcfsegment.h"

#include <iostream>

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
    std::cerr << "Invalid DCF line: " << line.toStdString() << std::endl;
}

int Performer::start()
{
    foreach (const QFileInfo &file, mDirectory.entryInfoList()) {
        std::cout << "Processing '" << file.fileName().toStdString() << "'..." << std::endl;
        DcfFile dcf(file.filePath());
        dcf.processFile(this);
    }
    std::cout << "Found " << mSegments.size() << " segments;" << std::endl;
    return 0;
}
