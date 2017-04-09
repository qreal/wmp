import {Command} from "./Command";
export class MultiCommand implements Command {

    private commands: Command[];

    constructor(commands: Command[]) {
        this.commands = commands;
    }

    public add(command: Command) {
        this.commands.push(command);
    }

    public execute(): void {
        this.commands.forEach((command: Command) => command.execute());
    }

    public revert(): void {
        for (var i = this.commands.length - 1; i >= 0; i--) {
            this.commands[i].revert();
        }
    }

    public isRevertible(): boolean {
        return this.commands.reduce((previousValue: boolean, command: Command): boolean => {
            return previousValue && command.isRevertible()
        }, true);
    }

}