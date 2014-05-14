'use strict';

// Products routes use platforms controller
var platforms = require('../controllers/platform');

module.exports = function(app) {

    app.get('/platforms', platforms.all);
    app.post('/platforms', platforms.create);
    app.get('/platforms/:platformId', platforms.findOne);
};
