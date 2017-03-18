import {Command} from "../model/commands/Command";
export class UndoRedoController {

    private stack: Command[];
    private pointer: number;
    private maxSize: number = 10000;
    private keyDownHandler: (event) => void;

    constructor() {
        this.stack = [];
        this.pointer = -1;
        var zKey: number = 90;
        this.keyDownHandler = (event) => {
            if ($("#diagram-area").is(":visible")) {
                if (event.keyCode == zKey && event.ctrlKey && event.shiftKey) {
                    event.preventDefault();
                    this.redo();
                } else if (event.keyCode == zKey && event.ctrlKey) {
                    event.preventDefault();
                    this.undo();
                }
            }
        };
        this.bindKeyboardHandler();
    }

    public addCommand(command: Command) {
        if (command.isRevertible()) {
            if (this.pointer < this.stack.length - 1) {
                this.popNCommands(this.stack.length - 1 - this.pointer);
            }
            this.stack.push(command);
            if (this.stack.length > this.maxSize) {
                this.stack.shift();
            } else {
                this.pointer++;
            }
        }
    }

    public undo(): void {
        if (this.pointer > -1) {
            this.stack[this.pointer].revert();
            this.pointer--;
        }
    }

    public redo(): void {
        if (this.pointer < this.stack.length - 1) {
            this.pointer++;
            this.stack[this.pointer].execute();
        }
    }

    public clearStack(): void {
        this.stack.splice(0, this.stack.length);
        this.pointer = -1;
    }

    public bindKeyboardHandler() {
        $(document).ready(() => {
            $(document).keydown(this.keyDownHandler);
        });
    }

    public unbindKeyboardHandler() {
        $(document).unbind('keydown', this.keyDownHandler);
    }

    private popNCommands(n: number) {
        while (n && this.stack.pop()) {
            n--;
        }
    }

}