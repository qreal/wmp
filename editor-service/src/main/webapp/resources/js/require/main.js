requirejs.config({
    baseUrl: 'resources/js/compiled',
    paths: {
        angular: '../libs/angular/angular',
        stompjs: '../libs/stompjs/stomp'
    },
    shim: {
        angular: {
            exports: 'angular',
        },
        stompjs: {
            exports: 'Stomp'
        }
    }
});

requirejs(
    ['angular', 'stompjs',
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

