#!/usr/bin/env node

'use strict';

/**
 * Module dependencies.
 */
var express = require('express'),
    logger = require('mean-logger'),
    expressLoad = require('express-load');

/**
 * Main application entry file.
 * Please note that the order of loading is important.
 */

// Load configurations
// Set the node enviornment variable if not set before
process.env.NODE_ENV = process.env.NODE_ENV || 'development';

// Initializing system variables 
var config = require('./config/config'),
    mongoose = require('mongoose');

// Bootstrap db connection
var db = mongoose.connect(config.db),
    app = express();

// Express settings
require('./config/express')(app, db);

// Bootstrap app
expressLoad('app/models', {
    extlist: /^(?!.*_spec\.).*\.(js$)/,
    cwd: __dirname
}).then('app/routes', {
    extlist: /(.*)\.(js$)/,
    cwd: __dirname
}).into(app);

// Start the app by listening on <port>
var port = process.env.PORT || config.port;
app.listen(port);
console.log('### Environment:', process.env.NODE_ENV);
console.log('Express app started on port ' + port + ' using config\n', config);

// Initializing logger
logger.init(app, {}, mongoose);

// Expose app
module.exports = app;
