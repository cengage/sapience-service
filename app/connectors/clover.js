'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    http = require('http'),
    _ = require('lodash'),
    S = require('string'),
    Q = require('q'),
    MetricModel = mongoose.model('Metric'),
    ConnectorModel = mongoose.model('Connector'),
    CategoryModel = mongoose.model('Category'),
    ProductCategoryModel = mongoose.model('ProductCategory');

function getValueFromProductCategory(productCategory) {

    var expression = productCategory.expression.split(',');

    var url = expression[0];

    var tagName = expression[1];

    var deferred = Q.defer();

    http.get(url, function(res) {

        var xml = '';

        res.on('data', function(chunk) {
            xml += chunk;
        });

        res.on('end', function() {

            var metrixData = xml.substring(170, 445);

            var coveredElementsPos, coveredElementsSelectedData, coveredElementsRequestedData,
                totalElementsPos, totalElementsSelectedData, totalElementsRequestedData,
                statementsPos, statementsSelectedData, statementsRequestedData,
                methodsPos, methodsSelectedData, methodsRequestedData,
                complexityPos, complexitySelectedData, complexityRequestedData;

            if (S(tagName).contains('Code Coverage')) {

                console.log('inside code coverage');

                coveredElementsPos = S(metrixData).indexOf('coveredelements');
                coveredElementsSelectedData = metrixData.substr(coveredElementsPos, metrixData.length);
                coveredElementsRequestedData = S(coveredElementsSelectedData).between('"', '"').s;

                console.log('covered element is ' + coveredElementsRequestedData);

                totalElementsPos = S(metrixData).indexOf(' elements');
                totalElementsSelectedData = metrixData.substr(totalElementsPos, metrixData.length);
                totalElementsRequestedData = S(totalElementsSelectedData).between('"', '"').s;

                console.log('total elements is ' + totalElementsRequestedData);

                var codeCoverage = S((S(coveredElementsRequestedData).toInt() / S(totalElementsRequestedData).toInt()) * 100).toString();

                console.log('final code coverage is ' + codeCoverage);

                deferred.resolve(codeCoverage);

            } else if (S(tagName).contains('Statements per Method')) {

                console.log('inside statement per method');

                statementsPos = S(metrixData).indexOf(' statements');
                statementsSelectedData = metrixData.substr(statementsPos, metrixData.length);
                statementsRequestedData = S(statementsSelectedData).between('"', '"').s;

                console.log('required statements is ' + statementsRequestedData);

                methodsPos = S(metrixData).indexOf('methods');
                methodsSelectedData = metrixData.substr(methodsPos, metrixData.length);
                methodsRequestedData = S(methodsSelectedData).between('"', '"').s;

                console.log('required methods is ' + methodsRequestedData);

                var statementsPerMethods = S(S(statementsRequestedData).toInt() / S(methodsRequestedData).toInt()).toString();

                console.log('final statementsPerMethods is ' + statementsPerMethods);

                deferred.resolve(statementsPerMethods);

            } else {

                console.log('inside cyclomatic complexity');

                complexityPos = S(metrixData).indexOf('complexity');
                complexitySelectedData = metrixData.substr(complexityPos, metrixData.length);
                complexityRequestedData = S(complexitySelectedData).between('"', '"').s;

                console.log('required complexity is ' + complexityRequestedData);

                methodsPos = S(metrixData).indexOf('methods');
                methodsSelectedData = metrixData.substr(methodsPos, metrixData.length);
                methodsRequestedData = S(methodsSelectedData).between('"', '"').s;

                console.log('required methods is ' + methodsRequestedData);

                var cyclomaticComplexity = S(S(complexityRequestedData).toInt() / S(methodsRequestedData).toInt()).toString();

                console.log('final cyclomaticComplexity is :  ' + cyclomaticComplexity);

                deferred.resolve(cyclomaticComplexity);
            }
        });
    }).on('error', function(e) {
        console.error('Got error: ' + e);
        deferred.reject(e);
    });

    return deferred.promise;
}

exports.fetch = function(req, res) {

    var metrics = [],
        fetchRequests = [],
        categoryIds = [];

    ConnectorModel.find({
        name: 'Clover'
    }, function(err, connectors) {

        CategoryModel.find({
            connector: connectors[0]._id
        }, function(err, categories) {

            _.each(categories, function(category) {
                categoryIds.push(category._id);
            });

            ProductCategoryModel.find({
                category: {
                    $in: categoryIds
                }
            }, function(err, productCategories) {

                console.log('The filtered ProductCategory list is : ' + productCategories);
                if (!err) {
                    _.each(productCategories, function(productCategory) {

                        var fetchReq = getValueFromProductCategory(productCategory);

                        fetchReq.then(function(jenkinsData) {
                            var metric = new MetricModel({
                                product: productCategory.product,
                                category: productCategory.category,
                                value: jenkinsData
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

        });
    });
};
