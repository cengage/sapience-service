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
    ProductCategoryModel = mongoose.model('ProductCategory');

function getValueFromProductCategory(productCategory) {

    var mainExpress = productCategory.expression;

    var expression = mainExpress.split(',');

    var url = expression[0];

    var tagName = expression[1];

    var deferred = Q.defer();

    console.log('main url is ' + url);

    http.get(url, function(res) {

        var xml = '';

        res.on('data', function(chunk) {

            xml += chunk;
        });

        res.on('end', function() {

            var pos, newSelectedData, requestedData, metrixData;
            if (S(mainExpress).contains('Cucumber')) {

                var totalCount = S(xml).count('classname="Then');

                deferred.resolve(totalCount);

            } else if (S(mainExpress).contains('Omni-PST-Build')) {

                pos = S(xml).indexOf('tests');
                newSelectedData = xml.substring(pos - 9, pos);

                requestedData = S(newSelectedData).replaceAll(' ', '').replaceAll(',', '').s;

                deferred.resolve(requestedData);

            } else if (S(mainExpress).contains('total')) {

                metrixData = xml.substring(39, 103);

                pos = S(metrixData).indexOf(tagName);

                newSelectedData = metrixData.substr(pos, metrixData.length);
                requestedData = S(newSelectedData).between('"', '"').s;

                deferred.resolve(requestedData);

            } else {

                metrixData = xml.substring(170, 445);

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

                console.log('response ended');
            }
        });

    });

    return deferred.promise;

}

exports.fetch = function(req, res) {
    var fetchRequests = [];
    ProductCategoryModel.find({}, function(err, productCategories) {
        if (!err) {
            var metrics = [];
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
};
