module.exports = function (grunt) {

    grunt.initConfig({
        ts: {
            all: {
                src: ["app/**"],
                outDir: "resources/js/compiled",
                options: {
                    module: 'amd',
                    target: 'es6'
                }
            }

        }
    });

    grunt.loadNpmTasks("grunt-ts");
    grunt.registerTask("default", ["ts:all"]);
}
