# LangBenchmark

## Requirements
* Java (8 and above)
* Gradle (6 and above)
* 7-Zip (to unpack data sets from 'Data' directory)

## For D
* dmd and dub (https://dlang.org/)

## For QT
* CMake (3.4 and above)

## For QT and C++
* Windows: MSVC (tested with 2019 community)
* Linux: gcc/g++ and make

## For DotNet
* dotnet (tested with 5.0 on Linux and from MSVC 2019 on Windows)

## How to launch:
**launch.bat** for Windows and **launch.sh** from Linux.

## Data sets
Data sets are placed in 'Data' directory (7z archives). You can prepare your own data set using the tool 'mp2dcf' which converts polish maps (*.mp) into *.dcf files. Also, you need pack all *.dcf files from your set to 7z archive and put them into the 'Data' directory.
