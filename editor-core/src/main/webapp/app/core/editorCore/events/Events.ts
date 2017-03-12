export class Events {
    private static isSignalsEnabled : boolean = true;

    static enableSignals() : void {
        Events.isSignalsEnabled = true;
    }

    static disableSignals() : void {
        Events.isSignalsEnabled = false;
    }

    static isSignalsEnagled() : boolean {
        return Events.isSignalsEnabled;
    }
}