using System;

namespace CSharp {
    internal class Position {
        public double Lat { get; set; }
        public double Lon { get; set; }

        public double GetDistance(Position other) {
            var lat1 = ToRadians(Lat);
            var lon1 = ToRadians(Lon);
            var lat2 = ToRadians(other.Lat);
            var lon2 = ToRadians(other.Lon);
            var sinLat = Math.Sin((lat2 - lat1) / 2.0);
            var sinLon = Math.Sin((lon2 - lon1) / 2.0);
            var a = sinLat * sinLat + Math.Cos(lat1) * Math.Cos(lat2) * sinLon * sinLon;
            return 2.0 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1.0 - a));
        }

        private static double ToRadians(double angle) {
            return Math.PI / 180 * angle;
        }
    }
}
