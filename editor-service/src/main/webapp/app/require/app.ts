/// <reference path="../vendor.d.ts" />
import angular = require('angular');
import {SelectorService} from "core/editorCore/service/SelectorService";

var app = angular
    .module('myApp', [])
    .factory('selectorService', () => new SelectorService($('#selectorsTmp').val()));
console.log('Creating module');
export = app;