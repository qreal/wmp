requirejs.config({
    baseUrl: 'resources/js/compiled',
    paths: {
        //angular
        angular: '../libs/angular/angular',
        jquery: './jquery',
        lodash: './lodash',
        backbone: './backbone',
    },
    shim: {
        angular: {
            exports: 'angular',
        }
    },
    map: {
        '*': {
            // Backbone requires underscore. This forces requireJS to load lodash instead:
            'underscore': 'lodash'
        }
    }
});

requirejs(
    ['angular',
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

