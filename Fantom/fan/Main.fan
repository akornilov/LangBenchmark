class Main {
    static Void main(Str[] args) {
        startTime := Duration.now()
        echo("Start...")
        Performer.make(args.isEmpty ? null : args[0]).start()
        totalTime := (Duration.now() - startTime).toMillis()
        echo("Finish: ${totalTime}ms")
    }
}
