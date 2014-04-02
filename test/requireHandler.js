if (process.env.APP_DIR_FOR_CODE_COVERAGE) {
    var extname = '.js',
        ext_super = require.extensions[extname],
        filePatternMatch = /\/app\/(?!.*\/test\/)(?!.*_spec\.).*\.(js$)/;
    require.extensions[extname] = function ext(module, filename) {
        filename = filename.match(filePatternMatch) ? filename.replace('/app/', process.env.APP_DIR_FOR_CODE_COVERAGE) : filename;
        return ext_super(module, filename);
    };
}
