'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
_ = require('lodash'),
    ProductModel = mongoose.model('Product');

/**
 * Find product by id
 */
exports.product = function (req, res, next, productId) {
    ProductModel.findById(productId, '-__v', function (err, product) {
        if (err) {
            return next(err);
        }
        else if (!product) {
            return next(new Error('Failed to load product ' + productId));
        }
        req.product = product;
        next();
    });
};

/**
 * Create an product
 */
exports.create = function (req, res) {
    var product = new ProductModel(req.body);

    product.save(function (err) {
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
exports.findOne = function (req, res) {
    res.jsonp(req.product);
};


/**
 * Update an product
 */
exports.update = function (req, res) {
    var product = req.product;

    product = _.extend(product, req.body);

    product.save(function (err) {
        if (err) {
            res.send(500, err);
        } else {
            res.jsonp(product);
        }
    });
};

/**
 * List of Products
 */
exports.all = function (req, res) {
    ProductModel.find({}, '-__v').exec(function (err, products) {
        if (err) {
            res.render('error', {
                status: 500
            });
        } else {
            res.jsonp(products);
        }
    });
};
