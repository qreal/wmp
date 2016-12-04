/// <reference path="../../../common/interfaces/editorCore.d.ts" />
/// <reference path="../../../common/interfaces/vendor.d.ts" />
/// <reference path="../../../../resources/thrift/editor/PaletteService_types.d.ts" />
/// <reference path="../../../../resources/thrift/editor/PaletteServiceThrift.d.ts" />
/// <reference path="../../../../resources/thrift/struct/Palette_types.d.ts" />
/// <reference path="../../../../resources/types/thrift/Thrift.d.ts" />
/// <reference path="../../../common/constants/GeneralConstants.ts" />
/// <reference path="../exporters/PaletteExporter.ts" />
/// <reference path="../parsers/PaletteParser.ts" />

class PaletteDiagramEditorController extends DiagramEditorController {

    private exporter: PaletteExporter;
    private parser: PaletteParser;

    constructor($scope, $attrs) {
        super($scope, $attrs);
        this.exporter = new PaletteExporter();
        this.parser = new PaletteParser(this);
        $scope.createPalette = () => { this.createPalette(); };
        this.elementsTypeLoader.load((elementTypes: ElementTypes): void => {
            this.handleLoadedTypes(elementTypes);
        }, "dsm");
    }

    public handleLoadedTypes(elementTypes: ElementTypes): void {
        this.propertyEditorController = new PropertyEditorController(this.sceneController, this.undoRedoController);

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

    public createPalette() {
        var name: string = prompt("input palette name");
        if (name !== "") {
            var controller = this;
            var palette = this.exporter.exportPalette(controller.getNodesMap(), controller.getLinksMap(), name);
            try {
                controller.getClient().createPalette(palette);
                controller.clearState();
                controller.paletteController.clearBlocksPalette();
                this.paletteController.appendBlocksPalette(controller.parser.parse(palette));
                this.paletteController.initDraggable();
                controller.addLinks();
            }
            catch (ouch) {
                console.log("Error: can't create palette", ouch);
            }
            /*$.ajax({
                type: 'POST',
                url: 'createPalette',
                contentType: 'application/json',
                data: JSON.stringify(paletteJson),
                success: function ():any {
                    console.log('ok');
                },
                error: function (response, status, error):any {
                    console.log("error: " + status + " " + error);
                }
            });*/
        }
    }

    public setNodeTypesMap(nodeTypes) {
        this.nodeTypesMap = nodeTypes;
    }

    private addLinks() {
        var properties: Map<Property> = {};
        properties["Guard"] = new Property("Guard", "combobox", "");
        var node: NodeType = new NodeType("Link", properties);
        this.nodeTypesMap["ControlFlow"] = node;
    }

    private getClient(): PaletteServiceThriftClient {
        var transport = new Thrift.TXHRTransport(GeneralConstants.PALETTE_REST_SERVLET);
        var protocol = new Thrift.TJSONProtocol(transport);
        return new PaletteServiceThriftClient(protocol);
    }
    /*
    public loadMetaEditor() {
        this.elementsTypeLoader.load((elementTypes: ElementTypes): void => {
            this.handleLoadedTypes(elementTypes);
        });
    }

    public setNodeTypesMap(nodeTypes) {
        this.nodeTypesMap = nodeTypes;
    }

    public choosePalette(paletteName: string) {
        var controller = this;
        $.ajax({
            type: 'POST',
            url: 'getPalette',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({name: paletteName}),
            success: function (response): any {
                controller.changePalette(controller.parser.parse(response));
                controller.addLinks();
            },
            error: function (response, status, error): any {
                console.log("error: " + status + " " + error);
            }
        })
    }

    public createPalette() {
        var name: string = prompt("input diagram name");
        if (name !== null && name !== "") {
            var controller = this;
            var paletteJson = this.exporter.exportPalette(controller.getNodesMap(), controller.getLinksMap(), name);
            $.ajax({
                type: 'POST',
                url: 'createPalette',
                contentType: 'application/json',
                data: JSON.stringify(paletteJson),
                success: function ():any {
                    controller.changePalette(controller.parser.parse(paletteJson));
                    controller.addLinks();
                    console.log('ok');
                },
                error: function (response, status, error):any {
                    console.log("error: " + status + " " + error);
                }
            });
        }
    }

    public showPaletteNames($compile, $scope): void {
        $("#palettes").empty();
        var meta = '<li><a href="" role="menuitem" tabindex="-1" ng-click="loadMetaEditor()">Meta Editor</a></li>';
        $("#palettes").append($compile(meta)($scope));
        $.ajax({
            type: 'POST',
            url: 'showPaletteNames',
            success: function (response):any {
                for (var i in response) {
                    var newPalette = '<li><a href="" role="menuitem" tabindex="-1" ng-click="choosePalette(' + "'" + response[i] + "'" + ')">' + response[i] + '</a></li>';
                    $("#palettes").append($compile(newPalette)($scope));
                }
            },
            error: function (response, status, error):any {
                console.log("error: " + status + " " + error);
            }
        });
    }

    private handleLoadedTypes(elementTypes: ElementTypes): void {
        this.propertyEditorController = new PropertyEditorController(this.paperController, this.undoRedoController);

        var categories: Map<Map<NodeType>> = elementTypes.paletteTypes.categories;
        for (var category in categories) {
            for (var typeName in categories[category]) {
                this.nodeTypesMap[typeName] = categories[category][typeName];
            }
        }

        this.addLinks();
        this.changePalette(elementTypes.paletteTypes);
    }

    private addLinks() {
        var properties: Map<NodeProperty> = {};
        properties["Guard"] = new NodeProperty("Guard", "combobox", "");
        var node: NodeType = new NodeType("Link", properties);
        this.nodeTypesMap["ControlFlow"] = node;
    }

    private changePalette(newPalette: PaletteTypes) {
        this.clearState();
        this.paletteController.clearBlocksPalette();
        this.paletteController.appendBlocksPalette(newPalette);
        this.paletteController.initDraggable();
    }*/

}