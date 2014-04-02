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
                require: ['test/requireHandler.js', 'test/mochaConfig.js', 'server.js']
            },
            integration: {
                src: ['test/integration/**/*Spec.js']
            }
        },
        env: {
            test: {
                NODE_ENV: 'test'
            },
            coverage: {
                APP_DIR_FOR_CODE_COVERAGE: '/test/coverage/instrument/app/'
            }
        },
        clean: {
            coverage: {
                src: ['test/coverage']
            }
        },
        instrument: {
            files: 'app/**/*.js',
            options: {
                lazy: true,
                basePath: 'test/coverage/instrument/'
            }
        },
        storeCoverage: {
            options: {
                dir: 'test/coverage/reports'
            }
        },
        makeReport: {
            src: 'test/coverage/reports/**/*.json',
            options: {
                type: 'lcov',
                dir: 'test/coverage/reports',
                print: 'detail'
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
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-istanbul');
    grunt.loadNpmTasks('grunt-mocha-test');
    grunt.loadNpmTasks('grunt-nodemon');
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-env');
    grunt.loadNpmTasks('grunt-jsbeautifier');

    //Making grunt default to force in order not to break the project.
    grunt.option('force', true);

    // Test task.
    grunt.registerTask('test', function() {
        grunt.option('force', false);
        grunt.task.run('env:test', 'jshint', 'mochaTest');
    });

    grunt.registerTask('coverage', function() {
        grunt.option('force', false);
        grunt.task.run('jshint', 'clean', 'env', 'instrument', 'mochaTest:integration', 'storeCoverage', 'makeReport');
    });

    //Default task(s).
    grunt.registerTask('default', ['coverage', 'jsbeautifier:default']);

    //Test task.
    grunt.registerTask('build', ['test', 'jsbeautifier:build']);

    // Server task
    grunt.registerTask('server', ['jshint', 'concurrent']);
    grunt.registerTask('server:prod', ['nodemon:prod']);
};
