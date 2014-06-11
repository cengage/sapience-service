'use strict';

// routes for users controller
var users = require('../controllers/user');

module.exports = function(app) {

    app.get('/users', users.all);
    app.post('/users', users.create);
    /*app.get('/users/:userId', users.show);*/
    app.get('/users/:email', users.findUserByEmailId);
    app.get('/users/:email/:password',users.findUserByEmailAndPwd);
    app.put('/users/:userId', users.update);

    app.param('userId', users.user);
};
