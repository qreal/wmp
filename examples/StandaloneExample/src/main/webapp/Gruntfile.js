module.exports = function (grunt) {

    grunt.initConfig({
        ts: {
            standaloneExample: {
                src: ["app/**/*.ts"],
                out: "resources/js/compiled/standalone-example.js"
            },
        }
    });

    grunt.loadNpmTasks("grunt-ts");
    grunt.registerTask("default", ["ts:standaloneExample"]);
}
