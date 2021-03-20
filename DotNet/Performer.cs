using System;
using System.Collections.Generic;
using System.IO;

namespace CSharp {
    internal class Performer {
        private readonly string _directory;
        private readonly Dictionary<string, List<DcfSegment> > _segments = new Dictionary<string, List<DcfSegment>>();
        private Position _pos;

        public Performer(string directory) {
            _directory = directory ?? ".";
        }

        public void Start() {
            string[] files;
            try {
                files = Directory.GetFiles(_directory, "*.dcf");
            } catch (Exception) {
                return;
            }
            foreach (var file in files) {
                Console.WriteLine("Processing '{0}'...", file.Replace("/", "\\"));
                new DcfFile(file).ProcessFile(SegmentRetreived, InvalidLine);
            }
            Console.WriteLine("Found {0} segments;", _segments.Count);
        }

        private bool SegmentRetreived(DcfSegment segment) {
            if (_pos == null) {
                _pos = segment.Start;
            }
            if (_segments.ContainsKey(segment.ClustId)) {
                _segments[segment.ClustId].Add(segment);
            } else {
                _segments[segment.ClustId] = new List<DcfSegment> { segment };
            }
            return true;
        }

        private static void InvalidLine(string line) {
            Console.Error.WriteLine("Invalid line DCF line: {0}", line);
        }
    }
}
