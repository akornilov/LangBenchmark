
shared void run() {
    value startTime = system.milliseconds;
    print("Start...");
    Performer(if (exists arg = process.arguments[0]) then arg else null).start();
    print("Finish: ``system.milliseconds - startTime``ms");
}