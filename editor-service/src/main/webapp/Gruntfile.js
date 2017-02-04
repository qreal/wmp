module.exports = function (grunt) {

    grunt.initConfig({
        ts: {
            all: {
                src: ["app/**"],
                outDir: "resources/js/compiled",
                options: {
                    module: 'amd'
                }
            }

        }
    });

    grunt.loadNpmTasks("grunt-ts");
    grunt.registerTask("default", ["ts:all"]);
}
