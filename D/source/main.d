@safe

import std.stdio;
import std.datetime.stopwatch;

import performer;

void main(string[] args) {
    writeln("Start...");
    scope StopWatch sw;
    sw.start();
    new Performer().start(args.length > 1 ? args[1] : null);
    writefln("Finish: %dms", sw.peek().total!"msecs");
}
