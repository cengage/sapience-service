'use strict';

// Products routes use products controller
var products = require('../controllers/product'),
    productCategories = require('../controllers/productCategory');

module.exports = function(app) {

    app.get('/products', products.all);
    app.post('/products', products.create);
    app.get('/products/:productId', products.findOne);

    app.get('/products/:productId/categories', productCategories.all);
    app.post('/products/:productId/categories', productCategories.create);
    //    app.post('/products/:productId/categories/:categoryId', productCategories.create);
};
