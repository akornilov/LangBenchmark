# LangBenchmark

## Requirements
* Java (8 and above)
* Gradle (6 and above)
* 7-Zip (to unpack data sets from 'Data' directory)

## For D
* dmd and dub (https://dlang.org/)

## For QT
* QT 5 itself
* CMake (3.4 and above)

## For QT and C++
* Windows: MSVC (tested with 2019 community)
* Linux: gcc/g++ and make

## For DotNet
* dotnet (tested with 5.0 on Linux and MSVC 2019 on Windows)

## For CSharp
* mono on Linux and MSVC on Windows (path to msbuild.exe should be in system PATH)

## For KotlinNative
* Ubuntu/Debian: sudo apt install libncurses5

## How to launch:
**launch.bat** for Windows and **launch.sh** from Linux.

## Data sets
Data sets are placed in 'Data' directory (7z archives). You can prepare your own data set using the tool 'mp2dcf' which converts polish maps (*.mp) into *.dcf files. Also, you need pack all *.dcf files from your set to 7z archive and put them into the 'Data' directory.

# Latest results

## Platforms and versions
* Linux Debian 4.19.171-2 (2021-01-30) x86_64 GNU/Linux
* Kotlin 1.4.31 (IR compiler)
* Java 1.8.0.281
* Scala 2.13.5
* C++ (Linux: gcc 8.3.0, Windows: MSVC 2019)
* D (dmd) 2.096.0
* Qt (Linux: 5.11.3, Windows: 5.12.10)
* DotNet (Linux: 5.0, Windows: MSVC 2019)
* CSharp (Linux: mono 6.12.0.122, Windows: MSVC 2019)
* KotlinNative 1.4.31

## Results for Linux:
  1. Kotlin: 1958ms, 1,00
  2. Java: 1965ms, 1,00
  3. Scala: 2022ms, 1,03
  4. C++: 2681ms, 1,37
  5. D: 3003ms, 1,53
  6. Qt: 3157ms, 1,61
  7. DotNet: 4120ms, 2,10
  8. CSharp: 5505ms, 2,81
  9. KotlinNative: 13320ms, 6,80
