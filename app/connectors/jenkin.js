'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    fs = require('fs'),
    et = require('elementtree'),
    http = require('http'),
    _ = require('lodash'),
    S = require('string'),

    MetricModel = mongoose.model('Metric'),
    ProductCategoryModel = mongoose.model('ProductCategory');

exports.fetch = function(req, res) {
    var fetchRequests = [];
    console.log('starting function');
    ProductCategoryModel
        .find({}, function(err, productCategories) {
            if (!err) {

                var metrics = [];

                _.each(productCategories, function(productCategory) {

                    var mainExpress = productCategory.expression;

                    var expression = mainExpress.split(',');

                    var url = expression[0];

                    var tagName = expression[1];

                    var requestedData;

                    console.log('main url is ' + url);

                    http.get(url, function(res) {

                        // save the data
                        var xml = '';

                        res.on('data', function(chunk) {
                            xml += chunk;

                        });

                        res.on('end', function() {
                            // parse xml
                            console.log('inside end function');

                            if (S(mainExpress).contains('Cucumber')) {

                                var totalCount = S(xml).count('classname="Then');

                                var metric = new MetricModel({
                                    product: productCategory.product,
                                    category: productCategory.category,
                                    value: totalCount
                                });

                                metric.save(function(err) {
                                    if (err) {
                                        console.error('### Saving to db', err);

                                    } else {
                                        console.log('### Saved data to db');

                                    }
                                });
                                metrics.push(metric);

                            } else if (S(mainExpress).contains('Omni-PST-Build')) {

                                var pos = S(xml).indexOf('tests');
                                var newSelectedData = xml.substring(pos - 9, pos);

                                var requestedData = S(newSelectedData).replaceAll(' ', '').replaceAll(',', '').s;

                                var metric = new MetricModel({
                                    product: productCategory.product,
                                    category: productCategory.category,
                                    value: requestedData
                                });

                                metric.save(function(err) {
                                    if (err) {
                                        console.error('### Saving to db', err);

                                    } else {
                                        console.log('### Saved data to db');

                                    }
                                });
                                metrics.push(metric);

                            } else if (S(mainExpress).contains('total')) {

                                var metrixData = xml.substring(39, 103);

                                var pos = S(metrixData).indexOf(tagName);

                                var newSelectedData = metrixData.substr(pos, metrixData.length);
                                var requestedData = S(newSelectedData).between('"', '"').s;

                                var metric = new MetricModel({
                                    product: productCategory.product,
                                    category: productCategory.category,
                                    value: requestedData
                                });

                                metric.save(function(err) {
                                    if (err) {
                                        console.error('### Saving to db', err);

                                    } else {
                                        console.log('### Saved data to db');

                                    }
                                });
                                metrics.push(metric);

                            } else {

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

                                    var metric = new MetricModel({
                                        product: productCategory.product,
                                        category: productCategory.category,
                                        value: codeCoverage
                                    });

                                    metric.save(function(err) {
                                        if (err) {
                                            console.error('### Saving to db', err);

                                        } else {
                                            console.log('### Saved data to db');

                                        }
                                    });
                                    metrics.push(metric);

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

                                    var metric = new MetricModel({
                                        product: productCategory.product,
                                        category: productCategory.category,
                                        value: statementsPerMethods
                                    });

                                    metric.save(function(err) {
                                        if (err) {
                                            console.error('### Saving to db', err);

                                        } else {
                                            console.log('### Saved data to db');

                                        }
                                    });
                                    metrics.push(metric);

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

                                    var metric = new MetricModel({
                                        product: productCategory.product,
                                        category: productCategory.category,
                                        value: cyclomaticComplexity
                                    });

                                    metric.save(function(err) {
                                        if (err) {
                                            console.error('### Saving to db', err);

                                        } else {
                                            console.log('### Saved data to db');

                                        }
                                    });

                                    metrics.push(metric);

                                }

                                console.log('response ended');
                            }
                        });

                    });

                });

                res.send(metrics);

            }
        });
};
