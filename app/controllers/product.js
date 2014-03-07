'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    ProductModel = mongoose.model('Product');

/**
 * Create an product
 */
exports.create = function(req, res) {
    var product = new ProductModel(req.body);

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
exports.findOne = function(req, res) {
    ProductModel.findById(req.params.productId, '-__v', function(err, product) {
        if (err) {
            res.send(500, err);
        }
        res.jsonp(product);
    });
};

/**
 * List of Products
 */
exports.all = function(req, res) {
    ProductModel.find({}, '-__v').exec(function(err, products) {
        if (err) {
            res.render('error', {
                status: 500
            });
        } else {
            res.jsonp(products);
        }
    });
};
