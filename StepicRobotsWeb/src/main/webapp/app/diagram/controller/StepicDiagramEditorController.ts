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

/// <reference path="../../diagramCore.d.ts" />
/// <reference path="../../vendor.d.ts" />

class StepicDiagramEditorController extends DiagramEditorController {

    private taskId: string;
    private kit: string;
    private isPaletteLoaded = false;
    private diagramJsonParser: DiagramJsonParser;
    private diagramExporter: DiagramExporter;
    private robotsDiagramNode: RobotsDiagramNode;

    constructor($scope, $attrs) {
        super($scope, $attrs);
        this.diagramJsonParser = new DiagramJsonParser();
        this.diagramExporter = new DiagramExporter();
        this.taskId = $attrs.task;
        this.kit = $attrs.kit;
        this.elementsTypeLoader.load((elementTypes: ElementTypes): void => {
            this.handleLoadedTypes(elementTypes);
        }, this.kit, this.taskId);
        $scope.submit = (): void => {
            this.submit(this.scope);
        };
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
        this.isPaletteLoaded = true;
        this.openDiagram(this.scope, this.kit, this.taskId);
    }

    public submit(scope: ng.IScope): void {
        if (!this.isPaletteLoaded) {
            alert("Palette is not loaded!");
            return;
        }
        $("#infoAlert").hide();
        var twoDModelSpinner = $('#twoDModelSpinner');
        twoDModelSpinner.show();
        var controller = this;
        var diagramParts: DiagramParts = new DiagramParts(this.getNodesMap(), this.getLinksMap(),
            this.robotsDiagramNode);
        $.ajax({
            type: 'POST',
            url: 'submit/' + controller.taskId,
            dataType: 'json',
            timeout: 60000,
            data: {
                'kit': controller.kit,
                'diagram': JSON.stringify(controller.diagramExporter.exportDiagramStateToJSON(this.getGraph(),
                    diagramParts))
            },
            success: function (response, status, jqXHR): any {
                twoDModelSpinner.hide();
                scope.$emit("emitCheckingResult", response);
            },
            error: function (response, status, error): any {
                twoDModelSpinner.hide();
                if (status == "timeout") {
                    alert("Timed out – please try again");
                } else {
                    alert(response.responseText);
                }
                console.log("error: " + status + " " + error);
            }
        });
    }

    public openDiagram(scope: ng.IScope, kit: string, taskId: string): void {
        if (!this.isPaletteLoaded) {
            alert("Palette is not loaded!");
            return;
        }
        var diagramSpinner = $('#diagramSpinner');
        diagramSpinner.show();
        var twoDModelSpinner = $('#twoDModelSpinner');
        twoDModelSpinner.show();
        var controller = this;
        $.ajax({
            type: 'POST',
            url: 'open/' + taskId,
            timeout: 60000,
            data: {
                'kit': kit
            },
            success: function (response, status, jqXHR): any {
                controller.clearState();
                diagramSpinner.hide();
                twoDModelSpinner.hide();
                scope.$emit("emit2dModelLoad", response.fieldXML);
                controller.handleLoadedDiagram(controller.diagramJsonParser.parse(response.diagram,
                    controller.nodeTypesMap));
            },
            error: function (response, status, error): any {
                diagramSpinner.hide();
                twoDModelSpinner.hide();
                alert("error: " + status + " " + error);
                console.log("error: " + status + " " + error);
            }
        });
    }

    public handleLoadedDiagram(diagramParts: DiagramParts): void {
        this.robotsDiagramNode = diagramParts.robotsDiagramNode;
        if (diagramParts.subprogramDiagramNodes.length > 0) {
            this.paletteController.appendSubprogramsPalette(diagramParts.subprogramDiagramNodes, this.nodeTypesMap);
        }

        var paper = this.diagramEditor.getPaper();
        paper.addNodesFromMap(diagramParts.nodesMap);
        paper.addLinksFromMap(diagramParts.linksMap);
        this.paletteController.initDraggable();
    }

}