'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

/**
 * User Schema
 */
var UserSchema = new Schema({
    firstName: {
        type: String,
        trim: true,
        required: true
    },
    lastName: {
        type: String,
        trim: true
    },
    email: {
    	type: String,
    	trim: true
    },
    password: {
    	type: String,
    	required: true
    },
    role: {
    	type: String,
    	trim: true
    },
    created: {
        type: Date,
        default: Date.now
    }
});

mongoose.model('User', UserSchema);
