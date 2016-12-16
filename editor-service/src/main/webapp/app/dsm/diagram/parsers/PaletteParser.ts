class PaletteParser {
    private controller: PaletteDiagramEditorController;

    constructor(controller: PaletteDiagramEditorController) {
        this.controller = controller;
    }

    public parse(json: any): PaletteTypes {
        var newPalette = new PaletteTypes();
        var basePalette: Map<NodeType> = {};
        var nodes = json.nodes;
        var nodeTypesMap = {};
        for (var i = 0; i < nodes.length; i++) {
            var nodeName = nodes[i].name;
            var nodeImage = nodes[i].image;
            var nodeProperties: Map<Property> = {};
            if (nodes[i].properties) {
                var properties = nodes[i].properties;
                for (var j = 0; j < properties.length; j++) {
                    var propertyName = properties[j].name;
                    var property: Property = new Property(propertyName, properties[j].type, properties[j].value);
                    nodeProperties[propertyName] = property;
                }
            }
            var node = new NodeType(nodeName, nodeProperties, nodeImage);
            basePalette[nodeName] = node;
            nodeTypesMap[nodeName] = node;
        }
        this.controller.setNodeTypesMap(nodeTypesMap);
        newPalette.categories[name] = basePalette;
        return(newPalette);
    }
}