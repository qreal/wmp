module.exports = function (grunt) {

    grunt.loadNpmTasks("grunt-ts");
    grunt.initConfig({
        ts: {
            all: {
                src: ["app/**/*.ts"],
                //Use out param and declaration true to generate one d.ts for the whole lib
                // out: "resources/js/compiled/editorCore.js",
                outDir: "resources/js/compiled/",
                options: {
                    // declaration: true,
                    module: 'amd',
                    target: 'es6'
                }
            }
        }
    });

    grunt.registerTask("default", ["ts:all"]);
};
