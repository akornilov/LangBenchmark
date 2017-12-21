using System;
using System.Diagnostics;

namespace CSharp {
    internal class Program {
        static void Main(string[] args) {
            var watch = new Stopwatch();
            watch.Start();
            Console.WriteLine("Start...");
            new Performer(args.Length > 0 ? args[0] : null).Start();
            watch.Stop();
            Console.WriteLine("Finish: {0}ms", watch.Elapsed.TotalMilliseconds);

            Console.ReadKey();
        }
    }
}
