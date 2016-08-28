module.exports = function (grunt) {

    grunt.initConfig({
        ts: {
            editorCore: {
                src: ["app/editor/core/**/*.ts"],
                out: "resources/js/compiled/editorCore.js"
            },
            twoDModelCore: {
                src: ["app/twoDModel/core/**/*.ts"],
                out: "resources/js/compiled/twoDModelCore.js"
            },
            editor: {
                src: ["app/editor/diagram/**/*.ts", "app/editor/*.ts"],
                out: "resources/js/compiled/editor.js"
            },
            undoRedoPlugin: {
                src: ["app/editor/plugins/undo-redo/**/*.ts"],
                out: "resources/js/compiled/undoRedoPlugin.js"
            },
            interpreter: {
                src: ["app/editor/interpreter/**/*.ts"],
                out: "resources/js/compiled/interpreter.js"
            },
            twoDModelRobots: {
                src: ["app/twoDModel/implementations/**/*.ts"],
                out: "resources/js/compiled/two-d-model-robots.js"
            }
        }
    });

    grunt.loadNpmTasks("grunt-ts");
    grunt.registerTask("default", ["ts:editorCore", "ts:twoDModelCore", "ts:editor", "ts:undoRedoPlugin", "ts:interpreter", "ts:twoDModelRobots"]);
}
