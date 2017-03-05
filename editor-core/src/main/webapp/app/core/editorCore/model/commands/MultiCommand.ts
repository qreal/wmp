import {Command} from "./Command";
export class MultiCommand implements Command {

    public static sideEffect : Command = null;

    private commands: Command[];

    constructor(commands: Command[]) {
        this.commands = commands;
    }

    public execute(): void {
        this.commands.forEach((command: Command) => command.execute());
        this.makeSideEffect();
    }

    public revert(): void {
        for (var i = this.commands.length - 1; i >= 0; i--) {
            this.commands[i].revert();
        }
        this.makeSideEffect();
    }

    public isRevertible(): boolean {
        return this.commands.reduce((previousValue: boolean, command: Command): boolean => {
            return previousValue && command.isRevertible()
        }, true);
    }

    private makeSideEffect() {
        if (MultiCommand.sideEffect != null) {
            MultiCommand.sideEffect.execute();
        }
    }

}