'use strict';

// Products routes use products controller
var products = require('../controllers/product'),
    productCategories = require('../controllers/productCategory'),
    jiraConnector = require('../connectors/jira'),
    jenkinsConnector = require('../connectors/jenkin'),
    cloverConnector=require('../connectors/clover'),
    sonarConnector=require('../connectors/sonar');

module.exports = function(app) {

    app.get('/jira/fetch', jiraConnector.fetch);
    app.get('/jenkins/fetch', jenkinsConnector.fetch);
    app.get('/clover/fetch', cloverConnector.fetch);
    app.get('/sonar/fetch', sonarConnector.fetch);

    app.get('/products', products.all);
    app.post('/products', products.create);
    app.get('/products/:productId', products.findOne);
    app.put('/products/:productId', products.update);

    app.get('/products/:productId/categories', productCategories.all);
    app.post('/products/:productId/categories', productCategories.create);
    //    app.post('/products/:productId/categories/:categoryId', productCategories.create);

    app.param('productId', products.product);
};
