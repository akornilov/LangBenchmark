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

### Common
* Kotlin 1.4.31 (IR compiler)
* KotlinNative 1.4.31
* Java 1.8.0.281
* Scala 2.13.5
* DotNet 5.0
* D dmd 2.096.0

### Windows 10.0.19041.868 (Win10 20H1 [2004] May 2020 Update)
* C++ msvc 19.28.29913
* Qt 5.12.10
* CSharp msvc 2019 16.9.2

### Linux Debian 4.19.171-2 (2021-01-30) x86_64 GNU/Linux
* C++ gcc 8.3.0
* Qt 5.11.3
* CSharp mono 6.12.0.122

## Results for Windows:
  1. Kotlin: 2147ms, 1,00
  2. Java: 2151ms, 1,00
  3. Scala: 2203ms, 1,03
  4. D: 4565ms, 2,13
  5. Qt: 4738ms, 2,21
  6. CSharp: 4748ms, 2,21
  7. C++: 4781ms, 2,23
  8. DotNet: 6096ms, 2,84
  9. KotlinNative: 30092ms, 14,02

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
