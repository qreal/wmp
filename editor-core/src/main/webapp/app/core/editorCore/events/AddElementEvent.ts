import {Command} from "../model/commands/Command";
import {Events} from "./Events";
export class AddElementEvent {

    private static subscriptions : Command[] = [];

    static subscribeEvent(command : Command) {
        AddElementEvent.subscriptions.push(command);
    }

    static signalEvent() {
        if (Events.isSignalsEnagled()) {
            for (let command of AddElementEvent.subscriptions) {
                command.execute();
            }
        }
    }

}
