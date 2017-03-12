import {Command} from "../model/commands/Command";
import {Events} from "./Events";
export class ChangeElementEvent {
    private static subscriptions : Command[] = [];

    static subscribeEvent(command : Command) {
        ChangeElementEvent.subscriptions.push(command);
    }

    static signalEvent() {
        if (Events.isSignalsEnagled()) {
            for (let command of this.subscriptions) {
                command.execute();
            }
        }
    }

}
