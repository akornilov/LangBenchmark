#include "dcffile.h"
#include "ieventhandler.h"
#include "dcfsegment.h"

#include <QTextStream>
#include <QDebug>

DcfFile::DcfFile(const QString &file) :
    mFile(file)
{
}

DcfFile::~DcfFile()
{
}

void DcfFile::processFile(IEventHandler *handler)
{
    if (handler && mFile.open(QIODevice::ReadOnly)) {
        QTextStream stream(&mFile);
        while (!stream.atEnd()) {
            const QString &line = stream.readLine().trimmed();
            if (line.isEmpty() || !line.startsWith("SEG:")) {
                continue;
            }
            const QStringList &parts = line.split(":");
            if (parts.size() < 6) {
                handler->invalidLine(line);
                continue;
            }
            auto ok = false;
            const auto lat1 = parts.at(2).toDouble(&ok);
            if (!ok) {
                handler->invalidLine(line);
                continue;
            }
            ok = false;
            const auto lon1 = parts.at(1).toDouble(&ok);
            if (!ok) {
                handler->invalidLine(line);
                continue;
            }
            ok = false;
            const auto lat2 = parts.at(4).toDouble(&ok);
            if (!ok) {
                handler->invalidLine(line);
                continue;
            }
            ok = false;
            const auto lon2 = parts.at(3).toDouble(&ok);
            if (!ok) {
                handler->invalidLine(line);
                continue;
            }
            auto index = -1;
            for (auto i = 5; i < parts.size(); i++) {
                if (parts.at(i).startsWith("ClustID")) {
                    index = i;
                    break;
                }
            }
            if (index < 0 || index + 1 >= parts.size()) {
                handler->invalidLine(line);
                continue;
            }
            DcfSegment segment(Position(lat1, lon1), Position(lat2, lon2), parts[index + 1]);
            if (!handler->segmentReceived(segment)) {
                break;
            }
        }
        mFile.close();
    }
}
