'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    _ = require('lodash'),
    UserModel = mongoose.model('User');

/**
 * Find user by id
 */
exports.user = function(req, res, next, userId) {
	UserModel.load(userId, function(err, user) {
        if (err) {
            return next(err);
        } else if (!user) {
            return next(new Error('Failed to load user ' + userId));
        }
        req.user = user;
        next();
    });
};

/**
 * Create an user
 */
exports.create = function(req, res) {
    var user = new UserModel(req.body);

    user.save(function(err) {
        if (err) {
            res.send(500, err);
        } else {
            res.jsonp(user);
        }
    });
};

/**
 * Load user by id
 */
exports.show = function(req, res) {
    res.jsonp(req.user);
};

/*Find user by email id*/

exports.findUserByEmailId = function(req, res) {
	
	 UserModel.find({email:req.params.email}, '-__v').exec(function(err, users) {
	        if (err) {
	            res.render('error', {
	                status: 500
	            });
	        } else {
	            res.jsonp(users);
	        }
	    });
};

/**
 * Find user by email and password
 */
exports.findUserByEmailAndPwd = function(req, res) {
    
    UserModel.find({email:req.params.email,password:req.params.password}, '-__v').exec(function(err, users) {
        if (err) {
            res.render('error', {
                status: 500
            });
        } else {
            res.jsonp(users);
        }
    });
};

/**
 * Update an user
 */

exports.update = function(req, res) {
    var user = req.body;

    user = _.extend(user, req.body);

    UserModel.update(function(err) {
        if (err) {
            res.send(500, err);
        } else {
            res.jsonp(user);
        }
    });
};


/**
 * List of Users
 */
exports.all = function(req, res) {
	UserModel.find({}, '-__v').exec(function(err, users) {
        if (err) {
            res.render('error', {
                status: 500
            });
        } else {
            res.jsonp(users);
        }
    });
};