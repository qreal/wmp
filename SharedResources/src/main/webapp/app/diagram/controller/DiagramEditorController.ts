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

/// <reference path="PaperController.ts" />
/// <reference path="PropertyEditorController.ts" />
/// <reference path="loaders/ElementsTypeLoader.ts" />
/// <reference path="PaletteController.ts" />
/// <reference path="parsers/DiagramJsonParser.ts" />
/// <reference path="exporters/DiagramExporter.ts" />
/// <reference path="../model/DiagramEditor.ts" />
/// <reference path="../model/RobotsDiagramNode.ts" />
/// <reference path="../model/Map.ts"/>
/// <reference path="../../vendor.d.ts" />

abstract class DiagramEditorController {

    protected scope: ng.IScope;
    protected diagramEditor: DiagramEditor;
    protected paperController: PaperController;
    protected propertyEditorController: PropertyEditorController;
    protected elementsTypeLoader: ElementsTypeLoader;
    protected paletteController: PaletteController;
    protected nodeTypesMap: Map<NodeType>;
    protected undoRedoController: UndoRedoController;

    constructor($scope, $attrs) {
        this.scope = $scope;
        this.undoRedoController = new UndoRedoController();
        this.nodeTypesMap = {};
        this.paletteController = new PaletteController();
        DiagramElementListener.getNodeProperties = (type: string): Map<Property> => {
            return this.getNodeProperties(type);
        }
        this.diagramEditor = new DiagramEditor();
        this.paperController = new PaperController(this, this.diagramEditor.getPaper());
        this.elementsTypeLoader = new ElementsTypeLoader();

        $scope.undo = () => {
            this.undoRedoController.undo();
        };

        $scope.redo = () => {
            this.undoRedoController.redo();
        };

        $(document).bind("mousedown", function (e) {
            if (!($(e.target).parents(".custom-menu").length > 0)) {
                $(".custom-menu").hide(100);
            }
        });
    }

    public getGraph(): joint.dia.Graph {
        return this.diagramEditor.getGraph();
    }

    public getNodesMap(): Map<DiagramNode> {
        var paper = this.diagramEditor.getPaper();
        return paper.getNodesMap();
    }

    public getLinksMap(): Map<Link> {
        var paper = this.diagramEditor.getPaper();
        return paper.getLinksMap();
    }

    public setNodeProperties(element: DiagramElement): void {
        this.propertyEditorController.setNodeProperties(element)
    }

    public clearNodeProperties(): void {
        this.propertyEditorController.clearState();
    }

    public getNodeType(type: string): NodeType {
        return this.nodeTypesMap[type];
    }

    public getNodeProperties(type: string): Map<Property> {
        return this.nodeTypesMap[type].getPropertiesMap();
    }

    public getUndoRedoController(): UndoRedoController {
        return this.undoRedoController;
    }

    public clearState(): void {
        this.propertyEditorController.clearState();
        this.paperController.clearState();
        this.diagramEditor.clear();
        this.undoRedoController.clearStack();
    }

}