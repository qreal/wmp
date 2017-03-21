requirejs.config({
    baseUrl: 'resources/js/compiled',
    paths: {
        angular: '../libs/angular/angular',

        jquery: '../libs/jquery/jquery',
        jqueryui: '../libs/jquery/ui/jquery-ui',
        jquerytree: '../libs/jquery/treeview/jquery.treeview',

        lodash: '../libs/lodash/lodash',
        backbone: '../libs/backbone/backbone',

        bootstrap: '../libs/bootstrap/bootstrap',

        jointjs: '../libs/joint/joint'
    },
    shim: {
        angular: {
            exports: 'angular',
        },
        jqueryui: ['jquery'],
        jquerytree: ['jquery'],
        bootstrap: ['jquery']

    },
    map: {
        '*': {
            'underscore': 'lodash'
        }
    }
});

requirejs(
    ['jointjs']
    , function (joint) {
        console.log('Adding joint to global');
        this.joint = joint;
    });

requirejs(
    ['angular', 'jointjs',
    'jquery', 'jqueryui', 'jquerytree',
    'lodash', 'backbone',
    'bootstrap',
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

