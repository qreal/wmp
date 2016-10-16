class RootDiagramController {

    constructor($scope, $compile) {
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
    }
}