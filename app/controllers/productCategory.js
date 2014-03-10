'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    ProductCategoryModel = mongoose.model('ProductCategory');

/**
 * Create an product
 */
exports.create = function(req, res) {

    var product = new ProductCategoryModel(req.body);
    product.product = req.params.productId;

    product.save(function(err) {
        if (err) {
            res.send(500, err);
        } else {
            res.jsonp(product);
        }
    });
};

/**
 * Load product by id
 */
/*
exports.findOne = function (req, res) {
    ProductCategoryModel.findById(req.params.categoryId, '-__v', function (err, product) {
        if (err) {
            res.send(500, err);
        }
        res.jsonp(product);
    });
};
*/

/**
 * List of Products
 */
exports.all = function(req, res) {
    ProductCategoryModel.find({
        product: req.params.productId
    }, '-__v').exec(function(err, products) {
        if (err) {
            res.render('error', {
                status: 500
            });
        } else {
            res.jsonp(products);
        }
    });
};
