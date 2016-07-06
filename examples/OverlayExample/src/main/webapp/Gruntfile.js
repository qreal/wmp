module.exports = function (grunt) {

    grunt.initConfig({
        ts: {
            overlayExample: {
                src: ["app/**/*.ts"],
                out: "resources/js/compiled/overlay-example.js"
            },
        }
    });

    grunt.loadNpmTasks("grunt-ts");
    grunt.registerTask("default", ["ts:overlayExample"]);
}
