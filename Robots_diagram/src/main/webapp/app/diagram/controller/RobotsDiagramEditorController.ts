/*
 * Copyright Vladimir Zakharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/// <reference path="DiagramMenuController.ts" />
/// <reference path="../../interpreter.d.ts" />
/// <reference path="../../diagramCore.d.ts" />
/// <reference path="../../vendor.d.ts" />

class RobotsDiagramEditorController extends DiagramEditorController {

    private diagramJsonParser: DiagramJsonParser;
    private menuController: DiagramMenuController;
    private diagramInterpreter: Interpreter;

    constructor($scope, $attrs) {
        super($scope, $attrs);
        this.diagramJsonParser = new RobotsDiagramJsonParser();
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

    public handleLoadedDiagramJson(diagramJson: any): void {
        var diagramParts: DiagramParts = this.diagramJsonParser.parse(diagramJson, this.nodeTypesMap);
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