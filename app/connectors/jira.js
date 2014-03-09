'use strict';

/**
 * Module dependencies.
 */
var /*mongoose = require('mongoose'),*/
    http = require('http'),
    Buffer = require('buffer').Buffer/*,
 MetricModel = mongoose.model('Metric'),
 ProductCategoryModel = mongoose.model('ProductCategory')*/;

/**
 * Fetch database from jira and store into Metrics collection
 */

exports.fetch = function (req, res) {
    var base64Encode = new Buffer('deekumar:Cengage15').toString('base64'),
        reqData = {
            jql: 'project = OPLAT AND priority = "Medium/Major" AND component = "GVRL 5" ORDER BY summary ASC',
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

    var fetchReq = http.request(options, function (jiraRes) {
        // Buffer the body entirely for processing as a whole.
        var dataChunks = [];
        jiraRes.on('data', function (chunk) {
            dataChunks.push(chunk);
        });

        jiraRes.on('end', function () {
            var data = dataChunks.join(''),
                jsonData = JSON.parse(data);
            console.log('Data fetched from jira,', jsonData);
            res.send(jsonData);
        });

    });
    fetchReq.on('error', function (e) {
        console.error('Got error: ' + e);
        res.send(500, e);
    });
    fetchReq.write(JSON.stringify(reqData));
    fetchReq.end();
};