using System;
using System.Globalization;
using System.IO;

namespace CSharp {
    internal class DcfFile {
        private readonly string _filename;

        public DcfFile(string filename) {
            _filename = filename;
        }

        public void ProcessFile(Func<DcfSegment, bool> segmentRetreived, Action<string> invalidLine) {
            try {
                using (var reader = new StreamReader(_filename)) {
                    while (!reader.EndOfStream) {
                        var line = reader.ReadLine();
                        if (string.IsNullOrEmpty(line)) {
                            continue;
                        }
                        var token = line.Trim();
                        if (string.IsNullOrEmpty(token) || !token.StartsWith("SEG:")) {
                            continue;
                        }
                        var parts = token.Split(':');
                        if (parts.Length < 6) {
                            invalidLine(line);
                            continue;
                        }
                        double lat1;
                        if (!double.TryParse(parts[2], NumberStyles.Any, CultureInfo.InvariantCulture, out lat1)) {
                            invalidLine(line);
                            continue;
                        }
                        double lon1;
                        if (!double.TryParse(parts[1], NumberStyles.Any, CultureInfo.InvariantCulture, out lon1)) {
                            invalidLine(line);
                            continue;
                        }
                        double lat2;
                        if (!double.TryParse(parts[4], NumberStyles.Any, CultureInfo.InvariantCulture, out lat2)) {
                            invalidLine(line);
                            continue;
                        }
                        double lon2;
                        if (!double.TryParse(parts[3], NumberStyles.Any, CultureInfo.InvariantCulture, out lon2)) {
                            invalidLine(line);
                            continue;
                        }
                        var index = -1;
                        for (var i = 5; i < parts.Length; i++) {
                            if (parts[i].StartsWith("ClustID")) {
                                index = i;
                                break;
                            }
                        }
                        if (index < 0 || index + 1 >= parts.Length) {
                            invalidLine(line);
                            continue;
                        }
                        if (!segmentRetreived(new DcfSegment(new Position { Lat = lat1, Lon = lon1 }, new Position { Lat = lat2, Lon = lon2 }, parts[index + 1]))) {
                            break;
                        }
                    }
                }
            } catch (Exception) {
                // ignored
            }
        }
    }
}
