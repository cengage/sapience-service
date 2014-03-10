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
    var base64Encode = new Buffer('deekumar:Cengage15').toString('base64'),
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
                    var data = dataChunks.join('');
                    console.log('Raw data from jira', data);

                    var jsonData = JSON.parse(data);
                    console.log('Data fetched from jira,', jsonData);
                    deferred.resolve(jsonData);
                });
            } else {
                deferred.reject(jiraRes.statusCode);
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
            _.each(productCategories, function(productCategory) {
                var fetchReq = getIssueCountForProductCategory(productCategory);

                fetchReq.then(function(jiraData) {
                    var metric = new MetricModel({
                        product: productCategory.product,
                        category: productCategory.category,
                        value: jiraData.total
                    });
                    metric.save();
                    return metric;
                });
                fetchRequests.push(fetchReq);
            });

            Q.all(fetchRequests).then(function(savedMetrics) {
                res.send(savedMetrics);
            }).fail(function(error) {
                res.send(500, error);
            });
        }
    });
};
