
namespace CSharp {
    internal class DcfSegment {
        public Position Start { get; }
        public Position End { get; }
        public string ClustId { get; }
        public double Length { get; }

        public DcfSegment(Position start, Position end, string clustId) {
            Start = start;
            End = end;
            ClustId = clustId;
            Length = start.GetDistance(end);
        }
    }
}
