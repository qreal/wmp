module.exports = function (grunt) {

    grunt.initConfig({
        ts: {
            editor: {
                src: ["app/robots/diagram/**/*.ts", "app/robots/*.ts", "app/robots/gestures/*.ts"],
                out: "resources/js/compiled/editor.js"
            },
            interpreter: {
                src: ["app/robots/interpreter/**/*.ts"],
                out: "resources/js/compiled/interpreter.js"
            },
            twoDModelRobots: {
                src: ["app/robots/twoDModel/**/*.ts"],
                out: "resources/js/compiled/two-d-model-robots.js"
            }
        }
    });

    grunt.loadNpmTasks("grunt-ts");
    grunt.registerTask("default", ["ts:editor", "ts:interpreter", "ts:twoDModelRobots"]);
}