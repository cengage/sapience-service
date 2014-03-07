'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    CategoryModel = mongoose.model('Category');

/**
 * Create an category
 */
exports.create = function(req, res) {
    var category = new CategoryModel(req.body);

    category.save(function(err) {
        if (err) {
            res.send(500, err);
        } else {
            res.jsonp(category);
        }
    });
};

/**
 * Load category by id
 */
exports.findOne = function(req, res) {
    CategoryModel.load(req.params.categoryId, function(err, category) {
        if (err) {
            res.send(500, err);
        }
        res.jsonp(category);
    });
};

/**
 * List of Categories
 */
exports.all = function(req, res) {
    CategoryModel.find({}, '-__v').exec(function(err, categories) {
        if (err) {
            res.render('error', {
                status: 500
            });
        } else {
            res.jsonp(categories);
        }
    });
};
