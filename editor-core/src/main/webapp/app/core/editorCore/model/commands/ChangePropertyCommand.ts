import {Command} from "./Command";
import {ChangeElementEvent} from "../../events/ChangeElementEvent";
export class ChangePropertyCommand implements Command {

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
    }

    public revert(): void {
        this.executionFunction(this.key, this.oldValue);
        this.changeHtmlFunction(this.oldValue);
    }

    public isRevertible() {
        return this.oldValue !== this.value;
    }
}