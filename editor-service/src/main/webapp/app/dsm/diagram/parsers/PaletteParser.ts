class PaletteParser {
    private controller: PaletteDiagramEditorController;

    constructor(controller: PaletteDiagramEditorController) {
        this.controller = controller;
    }

    public parse(typesJson: any): ElementTypes {
        var diagramElementTypes: ElementTypes = new ElementTypes();
        diagramElementTypes.blockTypes = this.parsePaletteTypes(typesJson.nodes);
        return diagramElementTypes;
    }

    private parsePaletteTypes(json: any): PaletteTree {
        var newPalette = new PaletteTree();
        var basePalette = new PaletteTree();
        for (var i = 0; i < json.length; i++) {
            var nodeName = json[i].name;
            var nodeImage = json[i].image;
            var nodeProperties: Map<Property> = {};
            if (json[i].properties) {
                var properties = json[i].properties;
                for (var j = 0; j < properties.length; j++) {
                    var propertyName = properties[j].name;
                    var property: Property = new Property(propertyName, properties[j].type, properties[j].value);
                    nodeProperties[propertyName] = property;
                }
            }
            var node = new NodeType(nodeName, nodeProperties, nodeImage);
            basePalette.nodes.push(node);
        }
        newPalette.categories["nodes"] = basePalette;
        return newPalette;
    }
}