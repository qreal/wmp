/// <reference path="DiagramMenuController.ts" />
/// <reference path="parsers/DiagramThriftParser.ts" />
/// <reference path="../../interfaces/interpreter.d.ts" />
/// <reference path="../../interfaces/diagramCore.d.ts" />
/// <reference path="../../interfaces/vendor.d.ts" />

class RobotsDiagramEditorController extends DiagramEditorController {

    private diagramParser: DiagramThriftParser;
    private menuController: DiagramMenuController;
    private diagramInterpreter: Interpreter;

    constructor($scope, $attrs) {
        super($scope, $attrs);
        this.diagramParser = new DiagramThriftParser();
        this.menuController = new DiagramMenuController(this);
        this.diagramInterpreter = new Interpreter();

        $scope.openTwoDModel = () => { this.openTwoDModel(); };
        $scope.createNewDiagram = () => { this.menuController.createNewDiagram(); };
        $scope.openFolderWindow = () => { this.menuController.openFolderWindow(); };
        $scope.saveCurrentDiagram = () => { this.menuController.saveCurrentDiagram(); };
        $scope.saveDiagramAs = () => { this.menuController.saveDiagramAs(); };
        $scope.clearAll = () => { this.clearAll(); };

        $scope.$on("interpret", (event, timeline) => {
            this.diagramInterpreter.interpret(this.getGraph(), this.getNodesMap(), this.getLinksMap(), timeline);
        });

        $scope.$on("stop", (event) => {
            this.diagramInterpreter.stop();
        });

        this.elementsTypeLoader.load((elementTypes: ElementTypes): void => {
            this.handleLoadedTypes(elementTypes);
        });
    }

    public handleLoadedTypes(elementTypes: ElementTypes): void {
        this.propertyEditorController = new PropertyEditorController(this.paperController, this.undoRedoController);

        for (var typeName in elementTypes.uncategorisedTypes) {
            this.nodeTypesMap[typeName] = elementTypes.uncategorisedTypes[typeName];
        }

        var categories: Map<Map<NodeType>> = elementTypes.paletteTypes.categories;
        for (var category in categories) {
            for (var typeName in categories[category]) {
                this.nodeTypesMap[typeName] = categories[category][typeName];
            }
        }

        this.paletteController.appendBlocksPalette(elementTypes.paletteTypes);
        this.paletteController.initDraggable();
    }

    public handleLoadedDiagramJson(diagram: DiagramDAO): void {
        var diagramParts: DiagramParts = this.diagramParser.parse(diagram, this.nodeTypesMap);
        var paper = this.diagramEditor.getPaper();
        paper.addNodesFromMap(diagramParts.nodesMap);
        paper.addLinksFromMap(diagramParts.linksMap);
    }

    public getDiagramParts(): DiagramParts {
        return new DiagramParts(this.getNodesMap(), this.getLinksMap());
    }

    public openTwoDModel(): void {
        $("#diagramContent").hide();
        $("#twoDModelContent").show();
    }

    public clearAll(): void {
        this.clearState();
        this.menuController.clearState();
    }

}