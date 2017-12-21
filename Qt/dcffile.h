#ifndef DCFFILE_H
#define DCFFILE_H

#include "ieventhandler.h"

#include "QString"
#include "QFile"

class DcfSegment;

class DcfFile
{
public:
    explicit DcfFile(const QString& file);
    virtual ~DcfFile();

    void processFile(IEventHandler* handler);

private:
    QFile mFile;
};

#endif // DCFFILE_H
