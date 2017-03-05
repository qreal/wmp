import {Command} from "./Command";
export class ChangePropertyCommand implements Command {

    public static sideEffect : Command = null;

    private key: string;
    private value: string;
    private oldValue: string;
    private executionFunction: (key: string, value: string) => void;
    private changeHtmlFunction: (value: string) => void;

    constructor(key: string, value: string, oldValue: string,
                executionFunction: (name: string, value: string) => void,
                changeHtmlFunction: (value: string) => void) {
        this.key = key;
        this.value = value;
        this.oldValue = oldValue;
        this.executionFunction = executionFunction;
        this.changeHtmlFunction = changeHtmlFunction;
    }

    public execute(): void {
        this.executionFunction(this.key, this.value);
        this.changeHtmlFunction(this.value);
        this.makeSideEffect()
    }

    public revert(): void {
        this.executionFunction(this.key, this.oldValue);
        this.changeHtmlFunction(this.oldValue);
        this.makeSideEffect()
    }

    public isRevertible() {
        return this.oldValue !== this.value;
    }

    private makeSideEffect() {
        if (ChangePropertyCommand.sideEffect != null) {
            ChangePropertyCommand.sideEffect.execute();
        }
    }

}