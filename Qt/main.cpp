#include "performer.h"

#include <iostream>

#include <QTime>

int main(int argc, char *argv[])
{
    QTime time;
    time.start();
    std::cout << "Start..." << std::endl;
    Performer performer(argc > 1 ? argv[1] : QString());
    const auto result = performer.start();
    std::cout << "Finish: " << time.elapsed() << "ms" << std::endl;
    return result;
}
