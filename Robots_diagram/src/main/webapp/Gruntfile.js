module.exports = function (grunt) {

    grunt.initConfig({
        ts: {
            diagramRobots: {
                src: ["app/diagram/**/*.ts", "app/constants/*.ts"],
                out: "resources/js/compiled/diagram-robots.js"
            },
            interpreter: {
                src: ["app/interpreter/**/*.ts"],
                out: "resources/js/compiled/interpreter.js"
            },
            twoDModelRobots: {
                src: ["app/twoDModel/**/*.ts"],
                out: "resources/js/compiled/two-d-model-robots.js"
            }
        }
    });

    grunt.loadNpmTasks("grunt-ts");
    grunt.registerTask("default", ["ts:diagramRobots", "ts:interpreter", "ts:twoDModelRobots"]);
}