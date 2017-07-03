/// <reference path="../../../../resources/thrift/editor/PaletteService_types.d.ts" />
/// <reference path="../../../../resources/thrift/editor/PaletteServiceThrift.d.ts" />
/// <reference path="../../../../resources/thrift/struct/Palette_types.d.ts" />
/// <reference path="../../../types/thrift/Thrift.d.ts" />
import {GeneralConstants} from "../../../common/constants/GeneralConstants";
import app = require("../../../require/app");
import {ElementTypes} from "core/editorCore/model/ElementTypes";
import {DiagramEditorController} from "core/editorCore/controller/DiagramEditorController";
import {PaletteExporter} from "../exporters/PaletteExporter";
import {PaletteParser} from "../parsers/PaletteParser";
import {PaletteView} from "../model/PaletteView";
import {ModelExporter} from "../exporters/ModelExporter"

export class PaletteDiagramEditorController extends DiagramEditorController {

    private exporter: PaletteExporter;
    private parser: PaletteParser;
    private palettes: PaletteView[] = [];
    private availableCreate: boolean = true;
    private availableGenerate: boolean = false;
    private modelExporter: ModelExporter;

    //Hack for firefox
    static $$ngIsClass: boolean;

    constructor($scope, $attrs) {
        super($scope, $attrs);

        this.exporter = new PaletteExporter();
        this.parser = new PaletteParser(this);
        this.modelExporter = new ModelExporter();

        $scope.createPalette = () => { this.createPalette(); };
        $scope.openPalettesMenu = () => { this.openPalettesMenu(); };
        $scope.loadMetaEditor = () => { this.loadMetaEditor(); };
        $scope.generate = () => { this.generate(); };
        this.elementsTypeLoader.load((elementTypes: ElementTypes): void => {
            this.handleLoadedTypes(elementTypes);
        }, "", "dsm");

        $("#elements-search").on('input', (event) => {
            this.paletteController.searchPaletteReload(event, this.elementTypes, this.nodeTypesMap);
            this.paletteController.initDraggable();
            this.paletteController.initClick(this.diagramEditor.getScene());
        } );

        var controller = this;
        try {
            var tPalettes: TPaletteView[] = controller.getClient().getPaletteViews();
            for (var i = 0; i < tPalettes.length; i++) {
                controller.palettes.push(PaletteView.createFromDAO(tPalettes[i]));
            }
        }
        catch (e) {
            console.log("Error: can't get folder tree", e);
        }
    }

    public createPalette() {
        if (this.availableCreate) {
            var name: string = prompt("input palette name");
            if (name !== null && name !== "") {
                var controller = this;
                var palette = this.exporter.exportPalette(this.getNodesMap(), this.getLinksMap(), name);
                try {
                    var id = this.getClient().createPalette(palette);
                    this.getClient().createMetamodel(palette);
                    this.clearState();
                    this.paletteController.clearBlocksPalette();
                    this.handleLoadedTypes(controller.parser.parse(palette));
                    this.palettes.push(new PaletteView(id, name));
                    this.availableCreate = false;
                    this.availableGenerate = true;
                }
                catch (e) {
                    console.log("Error: can't create palette", e);
                }
            }
        }
    }

    public openPalettesMenu() {
        var controller = this;
        var paletteNames: string[] = [];
        $('#palettesMenu').modal('show');
        for (var i = 0; i < this.palettes.length; i++) {
            paletteNames.push(this.palettes[i].getName());
        }
        $('.palette-table li').remove();

        $.each(paletteNames, function (i) {
            $('.palette-view ul').prepend("<li class='palettes'>" +
                "<span class='glyphicon glyphicon-file' aria-hidden='true'></span>" +
                "<span class='glyphicon-class'>" + paletteNames[i] + "</span></li>");
        });

        $('.palette-table .palettes').click(function () {
            controller.loadPalette(($(this).text()));
            $('#palettesMenu').modal('hide');
        });
    }

    public setNodeTypesMap(nodeTypes) {
        this.nodeTypesMap = nodeTypes;
    }

    public loadPalette(paletteName: string) {
        var controller = this;
        try {
            var palette = controller.getClient().loadPalette(controller.getPaletteIdByName(paletteName));
            controller.clearState();
            controller.paletteController.clearBlocksPalette();
            controller.handleLoadedTypes(controller.parser.parse(palette));
            controller.availableCreate = false;
        }
        catch (e) {
            console.log("Error: can't load palette", e);
        }
    }

    public loadMetaEditor() {
        this.availableCreate = true;
        this.availableGenerate = false;
        this.clearState();
        this.paletteController.clearBlocksPalette();
        this.elementsTypeLoader.load((elementTypes: ElementTypes): void => {
            this.handleLoadedTypes(elementTypes);
        }, "", "dsm");
    }

    public generate() {
        if (this.availableGenerate) {
            var name: string = prompt("input model name");
            var model = this.modelExporter.exportModel(this.getNodesMap(), this.getLinksMap(), name);
            try {
                this.getClient().createModel(model);
            }
            catch (ouch) {
                console.log("Error: can't save diagram");
            }
        }
    }

    private getPaletteIdByName(paletteName: string) {
        for (var i = 0; i < this.palettes.length; i++) {
            if ( this.palettes[i].getName() === paletteName)
                return  this.palettes[i].getId();
        }
    }

    private getClient(): PaletteServiceThriftClient {
        var transport = new Thrift.TXHRTransport(GeneralConstants.PALETTE_REST_SERVLET);
        var protocol = new Thrift.TJSONProtocol(transport);
        return new PaletteServiceThriftClient(protocol);
    }
}

PaletteDiagramEditorController.$$ngIsClass = true;
app.controller("PaletteDiagramEditorController", PaletteDiagramEditorController);
console.log("Adding controller PaletteDiagramEditorController");