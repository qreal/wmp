requirejs.config({
    baseUrl: 'resources/js/compiled',
    paths: {
        //angular
        angular: '../libs/angular/angular',
        lodash: '../libs/lodash/lodash',
        backbone: '../libs/backbone/backbone',
    },
    shim: {
        angular: {
            exports: 'angular',
        },
        jquery: {
            exports: '$'
        },
    },
    map: {
        '*': {
            // Backbone requires underscore. This forces requireJS to load lodash instead:
            'underscore': 'lodash'
        }
    }
});

requirejs(
    ['angular','jquery', 'lodash', 'backbone','joint',
    'require/app',
    'robots/RootDiagramController',
    'robots/diagram/controller/RobotsDiagramEditorController',
    'robots/twoDModel/implementations/engine/TwoDModelEngineFacadeImpl',
    'bpmn/diagram/controller/BpmnDiagramEditorController',
    'core/editorCore/controller/DiagramEditorController']
    , function (angular) {
    console.log('Bootstraping Angular called');
    angular.bootstrap(document, ['myApp']);
});

