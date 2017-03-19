import app = require("../require/app");
app.controller('RootDiagramController', ['$scope',  function ($scope) {
    $scope.root = this;
    $scope.$on("emitCheckingResult", (event, result) => {
        $scope.$broadcast("displayCheckingResult", result);
    });
    $scope.$on("emit2dModelLoad", (event, fieldXML) => {
        $scope.$broadcast("2dModelLoad", fieldXML);
    });
    $scope.$on("emitInterpret", (event, timeline) => {
        $scope.$broadcast("interpret", timeline);
    });
    $scope.$on("emitStop", (event) => {
        $scope.$broadcast("stop");
    });
}]);
console.log("Adding controller RootDiagramController");