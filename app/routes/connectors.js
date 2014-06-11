'use strict';

// Products routes use connectors controller
var connectors = require('../controllers/connector');

module.exports = function(app) {

    app.get('/connectors', connectors.all);
    app.post('/connectors', connectors.create);
    app.get('/connectors/:connectorId', connectors.findOne);
};