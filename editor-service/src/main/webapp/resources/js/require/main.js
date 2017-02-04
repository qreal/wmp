requirejs.config({
    baseUrl: 'resources/js/compiled',
    paths: {
        angular: 'angular'
    },
    shim: {
        angular: {
            exports: 'angular'
        }
    }
});

requirejs(
    ['angular', 'require/app',
    'robots/RootDiagramController',
    'robots/diagram/controller/RobotsDiagramEditorController',
    'robots/twoDModel/implementations/engine/TwoDModelEngineFacadeImpl',
    'bpmn/diagram/controller/BpmnDiagramEditorController',
    'core/editorCore/controller/DiagramEditorController']
    , function (angular, app, root) {
    console.log('Bootstraping Angular called');
    console.log(root.toString());
    angular.bootstrap(document, ['myApp']);
});

