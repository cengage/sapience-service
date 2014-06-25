'use strict';

// Products routes use metrics controller
var targetMetrics = require('../controllers/targetMetrics');

module.exports = function(app) {

    app.get('/targetMetrics', targetMetrics.all);
    app.post('/targetMetrics', targetMetrics.create);
};
