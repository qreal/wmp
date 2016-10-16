/// <reference path="../interfaces/plugins/gestures.d.ts" />
/// <reference path="../interfaces/plugins/interpreter.d.ts" />
/// <reference path="../interfaces/plugins/undo-redo.d.ts" />
/// <reference path="../interfaces/gesturesScene.ts" />

class PluginController {
    public static create(className: string, ...args: any[]) {
        if (!this.isBuilt(className))
            return null;
        var createdClass = window[className];
        switch (className) {
            case "GesturesController":
                return new createdClass(args[0], args[1]);
            case "Interpreter":
                return new createdClass();
            case "UndoRedoController":
                return new createdClass();
        }
        return null;
    }
    public static exec(object: Object, method: string, ...args: any[]) {
        if (!object || typeof object[method] !== 'function')
            return null;
        var func = object[method].bind(object);
        if (this.isBuilt("GesturesController") && GesturesController.prototype.isPrototypeOf(object)) {
            switch (method) {
                case "startDrawing":
                    return func();
                case "onMouseMove":
                    return func(args[0]);
                case "onMouseDown":
                    return func(args[0]);
                case "onMouseUp":
                    return func(args[0]);
            }
        }
        if (this.isBuilt("Interpreter") && Interpreter.prototype.isPrototypeOf(object)) {
            switch (method) {
                case "interpret":
                    return func(args[0], args[1], args[2], args[3]);
            }
        }
        if (this.isBuilt("UndoRedoController") && UndoRedoController.prototype.isPrototypeOf(object)) {
            switch (method) {
                case "addCommand":
                    return func(args[0]);
                case "undo":
                    return func();
                case "redo":
                    return func();
                case "clearStack":
                    return func();
                case "bindKeyboardHandler":
                    return func();
                case "unbindKeyboardHandler":
                    return func();
            }
        }
        return null;
    }
    private static isBuilt(className: string) {
        return (window.hasOwnProperty(className));
    }
}
