'use strict';

module.exports = function(grunt) {
    // Project Configuration
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        watch: {
            options: {
                livereload: 35720
            },
            js: {
                files: ['<%= jshint.all.src %>'],
                tasks: ['jshint']
            }
        },
        jshint: {
            all: {
                src: ['gruntfile.js', 'server.js', 'app/**/*.js', 'config/**/*.js', '!test/coverage/**/*.js'],
                options: {
                    jshintrc: true
                }
            }
        },
        nodemon: {
            dev: {
                script: 'server.js',
                options: {
                    ext: 'js,json',
                    nodeArgs: ['--debug=7878']
                }
            },
            prod: {
                script: 'server.js',
                options: {
                    ignore: ['.'],
                    env: {
                        NODE_ENV: 'production'
                    }
                }
            }
        },
        concurrent: {
            tasks: ['nodemon:dev', 'watch'],
            options: {
                logConcurrentOutput: true
            }
        },
        mochaTest: {
            options: {
                reporter: 'spec',
                require: 'server.js'
            },
            src: ['**/*_spec.js']
        },
        env: {
            test: {
                NODE_ENV: 'test'
            }
        },
        jsbeautifier: {
            'default': {
                src: ['<%= jshint.all.src %>', 'package.json'],
                options: {
                    js: {
                        preserveNewlines: true,
                        maxPreserveNewlines: 2
                    }
                }
            },
            'build': {
                src: '<%= jsbeautifier.default.src %>',
                options: {
                    mode: 'VERIFY_ONLY',
                    js: '<%= jsbeautifier.default.options.js%>'
                }
            }
        }
    });

    //Load NPM tasks
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-mocha-test');
    grunt.loadNpmTasks('grunt-nodemon');
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-env');
    grunt.loadNpmTasks('grunt-jsbeautifier');

    //Making grunt default to force in order not to break the project.
    grunt.option('force', true);

    //Default task(s).
    grunt.registerTask('default', [ /*'test',*/ 'jsbeautifier:default']);

    //Test task.
    grunt.registerTask('test', ['env:test', 'mochaTest']);
    grunt.registerTask('build', [ /*'test',*/ 'jsbeautifier:build']);

    // Server task
    grunt.registerTask('server', ['jshint', 'concurrent']);
    grunt.registerTask('server:prod', ['nodemon:prod']);
};
