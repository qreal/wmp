module.exports = function (grunt) {

    grunt.initConfig({
        ts: {
            root: {
                src: ["app/*.ts"],
                out: "resources/js/compiled/root.js"
            },
            diagramCore: {
                src: ["app/diagram/**/*.ts"],
                out: "resources/js/compiled/diagram-core.js"
            },
            twoDModelCore: {
                src: ["app/twoDModel/**/*.ts"],
                out: "resources/js/compiled/two-d-model-core.js"
            },
            utils: {
                src: ["app/utils/**/*.ts"],
                out: "resources/js/compiled/utils.js"
            }
        }
    });

    grunt.loadNpmTasks("grunt-ts");
    grunt.registerTask("default", ["ts:root", "ts:diagramCore", "ts:twoDModelCore", "ts:utils"]);
}
