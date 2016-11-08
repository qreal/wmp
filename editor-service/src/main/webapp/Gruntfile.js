module.exports = function (grunt) {

    grunt.initConfig({
        ts: {
            root: {
                src: ["app/robots/*.ts"],
                out: "resources/js/compiled/root.js"
            },
            robotsEditor: {
                src: ["app/robots/diagram/**/*.ts"],
                out: "resources/js/compiled/robotsEditor.js"
            },
            dsm: {
                src: ["app/dsm/diagram/**/*.ts"],
                out: "resources/js/compiled/dsm.js"
            },
            interpreter: {
                src: ["app/robots/interpreter/**/*.ts"],
                out: "resources/js/compiled/interpreter.js"
            },
            twoDModelRobots: {
                src: ["app/robots/twoDModel/**/*.ts"],
                out: "resources/js/compiled/two-d-model-robots.js"
            },
            gestures: {
                src: ["app/common/gestures/*.ts"],
                out: "resources/js/compiled/gestures.js"
            },
            menu: {
                src: ["app/common/menu/**/*.ts"],
                out: "resources/js/compiled/menu.js"
            }
        }
    });

    grunt.loadNpmTasks("grunt-ts");
    grunt.registerTask("default", ["ts:root", "ts:interpreter", "ts:twoDModelRobots", "ts:gestures", "ts:menu", "ts:robotsEditor", "ts:dsm"]);
}