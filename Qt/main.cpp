#include "performer.h"

#include <QTime>
#include <QDebug>

int main(int argc, char *argv[])
{
    QTime time;
    time.start();
    qDebug() << "Start...";
    Performer performer(argc > 1 ? argv[1] : QString());
    const auto result = performer.start();
    qDebug() << "Finish:" << time.elapsed() << "ms";
    return result;
}
