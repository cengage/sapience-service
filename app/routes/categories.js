'use strict';

// Products routes use categories controller
var categories = require('../controllers/category');

module.exports = function(app) {

    app.get('/categories', categories.all);
    app.post('/categories', categories.create);
    app.get('/categories/:categoryId', categories.show);
    app.put('/categories/:categoryId', categories.update);

    app.param('categoryId', categories.category);
};
