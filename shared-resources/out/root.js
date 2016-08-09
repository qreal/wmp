var RootDiagramController = (function () {
    function RootDiagramController($scope, $compile) {
        $scope.root = this;
        $scope.$on("emitCheckingResult", function (event, result) {
            $scope.$broadcast("displayCheckingResult", result);
        });
        $scope.$on("emit2dModelLoad", function (event, fieldXML) {
            $scope.$broadcast("2dModelLoad", fieldXML);
        });
        $scope.$on("emitInterpret", function (event, timeline) {
            $scope.$broadcast("interpret", timeline);
        });
        $scope.$on("emitStop", function (event) {
            $scope.$broadcast("stop");
        });
    }
    return RootDiagramController;
})();
//# sourceMappingURL=root.js.map