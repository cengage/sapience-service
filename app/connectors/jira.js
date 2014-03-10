'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    http = require('http'),
    Q = require('q'),
    _ = require('lodash'),
    Buffer = require('buffer').Buffer,
    MetricModel = mongoose.model('Metric'),
    ProductCategoryModel = mongoose.model('ProductCategory');

function getIssueCountForProductCategory(productCategory) {
    var base64Encode = new Buffer('qametrics:quality').toString('base64'),
        reqData = {
            jql: productCategory.expression,
            maxResults: 0
        },
        options = {
            host: 'jira.cengage.com',
            port: '80',
            method: 'POST',
            path: '/rest/api/2/search',
            headers: {
                'Authorization': 'Basic ' + base64Encode,
                'Content-Type': 'application/json'
            }
        };

    var deferred = Q.defer(),
        fetchReq = http.request(options, function(jiraRes) {
            if (jiraRes.statusCode === 200) {
                // Buffer the body entirely for processing as a whole.
                var dataChunks = [];
                jiraRes.on('data', function(chunk) {
                    dataChunks.push(chunk);
                });

                jiraRes.on('end', function() {
                    var data = dataChunks.join(''),
                        jsonData = JSON.parse(data);
                    console.log('Data fetched from jira,', jsonData, 'for expression=', productCategory.expression);
                    deferred.resolve(jsonData);
                });
            } else {
                deferred.reject({
                    statusCode: jiraRes.statusCode,
                    productCategory: productCategory
                });
            }
        }).on('error', function(e) {
            console.error('Got error: ' + e);
            deferred.reject(e);
        });

    fetchReq.write(JSON.stringify(reqData));
    fetchReq.end();
    return deferred.promise;
}
/**
 * Fetch database from jira and store into Metrics collection
 */

exports.fetch = function(req, res) {
    var fetchRequests = [];
    ProductCategoryModel.find({}, function(err, productCategories) {
        if (!err) {
            var metrics = [];
            _.each(productCategories, function(productCategory) {
                if (!_.contains(['531a25bbca22376bb3500fc2', '531a21b13aca8a1fae0603c1'], productCategory.product.toString())) {
                    var fetchReq = getIssueCountForProductCategory(productCategory);

                    fetchReq.then(function(jiraData) {
                        var metric = new MetricModel({
                            product: productCategory.product,
                            category: productCategory.category,
                            value: jiraData.total
                        });
                        metric.save(function(err) {
                            if (err) {
                                console.error('### Saving to db', err);

                            } else {
                                console.log('### Saved data to db');

                            }
                        });
                        metrics.push(metric);
                    });
                    fetchRequests.push(fetchReq);
                }
            });

            Q.all(fetchRequests).then(function() {
                res.send(metrics);
            }).fail(function(error) {
                console.error(arguments);
                res.send(500, {
                    error: error.stacktrace || error,
                    data: metrics
                });
            });
        }
    });
};
