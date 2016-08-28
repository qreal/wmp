declare class UndoRedoController {

    public addCommand(command: Command);
    public undo(): void;
    public redo(): void;
    public clearStack(): void;
    public bindKeyboardHandler();
    public unbindKeyboardHandler();

}