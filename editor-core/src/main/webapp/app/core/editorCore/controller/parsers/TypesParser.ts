/// <reference path="../../model/Map.ts" />
/// <reference path="../../model/NodeType.ts" />
/// <reference path="../../model/ElementTypes.ts" />
/// <reference path="../../../../common/constants/GeneralConstants.d.ts" />
/// <reference path="../../../../vendor.d.ts" />

class TypesParser {

    private currentProperties: Map<Property>;
    private linkPatterns: Map<joint.dia.Link>;
    private currentImage: string;

    public parse(typesJson: any): ElementTypes {
        var diagramElementTypes: ElementTypes = new ElementTypes();
        this.linkPatterns = {};
        diagramElementTypes.containerTypes = this.parseGeneralTypes(typesJson.blocks.containers, "containers");
        typesJson.blocks.containers = null;
        diagramElementTypes.blockTypes = this.parsePaletteTypes(typesJson.blocks);
        diagramElementTypes.flowTypes = this.parseGeneralTypes(typesJson.flows, "flows");
        var flowsMap: Map<NodeType> = diagramElementTypes.flowTypes.convertToMap();
        for (var flow in flowsMap) {
            if (!this.linkPatterns[flow])
                this.linkPatterns[flow] = new joint.dia.Link({
                    attrs: {
                        '.connection': { stroke: 'black' },
                        '.marker-target': { fill: 'black', d: 'M 10 0 L 0 5 L 10 10 z' }
                    }
                });
        }
        diagramElementTypes.linkPatterns = this.linkPatterns;
        return diagramElementTypes;
    }

    private parseGeneralTypes(elementsTypes: any, category: string): PaletteTree {
        var elementsTree: PaletteTree = new PaletteTree();
        var elements: PaletteTree = new PaletteTree();
        elementsTree.categories[category] = elements;

        for (var i in elementsTypes) {
            var typeObject = elementsTypes[i];
            elements.nodes = elements.nodes.concat(this.createNodeTypes(typeObject).nodes);
        }
        return elementsTree;
    }

    private parsePaletteTypes(paletteTypes: any): PaletteTree {
        var paletteTypesObject: PaletteTree = new PaletteTree();

        for (var category in paletteTypes) {
            var categoryTree: PaletteTree = new PaletteTree();
            for (var i in paletteTypes[category]) {
                var typeObject = paletteTypes[category][i];
                var typeTree: PaletteTree = this.createNodeTypes(typeObject);
                if (!typeObject.subtypes)
                    categoryTree.nodes = categoryTree.nodes.concat(typeTree.nodes);
                else
                    categoryTree.categories[typeObject.type] = typeTree;
            }
            paletteTypesObject.categories[category] = categoryTree;
        }

        return paletteTypesObject;
    }

    private createNodeTypes(typeObject: any): PaletteTree {
        var nodesTree: PaletteTree;
        var name: string = typeObject.name;
        var typeName: string = typeObject.type.toLowerCase();

        this.currentProperties = this.parseTypeProperties(typeName, typeObject.properties);

        this.currentImage = "";
        if (typeObject.image)
            this.currentImage = GeneralConstants.APP_ROOT_PATH + typeObject.image.src;

        if (typeObject.attrs)
            this.linkPatterns[typeName.toLowerCase()] = new joint.dia.Link({attrs: typeObject.attrs});

        var categories: any = typeObject.subtypes;
        if (!categories) {
            var node: NodeType = new NodeType(name, this.currentProperties, this.currentImage, [typeName]);
            nodesTree = new PaletteTree();
            nodesTree.nodes.push(node);
        } else
            nodesTree = this.parseSubtypes(categories, [typeName]);

        return nodesTree;
    }

    private parseSubtypes(categories: any, path: string[]): PaletteTree {
        var nodesTree: PaletteTree = new PaletteTree();
        if (categories instanceof Array) {
            for (var i in categories) {
                var subtype: string = categories[i];
                path.push(subtype.toLowerCase());
                nodesTree.nodes.push(new NodeType(subtype, this.currentProperties, this.currentImage, path));
                path.pop();
            }
        } else {
            for (var category in categories) {
                path.push(category.toLowerCase());
                nodesTree.categories[category] = this.parseSubtypes(categories[category], path);
                path.pop();
            }
        }

        return nodesTree;
    }

    private parseTypeProperties(typeName: string, propertiesArrayNode: any): Map<Property> {
        var properties: Map<Property> = {};
        for (var i in propertiesArrayNode) {
            var propertyObject = propertiesArrayNode[i];
            var propertyKey: string = propertyObject.key;
            var propertyName: string = propertyObject.name;
            var propertyType: string = propertyObject.type;

            if (propertyType === "dropdown" || propertyType === "combobox" || propertyType === "checkbox") {
                this.addVariantList(typeName, propertyKey, propertyObject.variants);
            }

            var propertyValue: string = "";
            if (propertyObject.value) {
                propertyValue = propertyObject.value;
            } else if (propertyObject.selected) {
                propertyValue = propertyObject.selected.key;
            }

            var property: Property = new Property(propertyName, propertyType, propertyValue);
            properties[propertyKey] = property;
        }
        return properties;
    }

    private addVariantList(typeName: string, propertyKey: string, variantsArrayNode: any): void {
        var variants: Variant[] = [];
        for (var i in variantsArrayNode) {
            var variant = variantsArrayNode[i];
            variants.push(new Variant(variant.key, variant.value));
        }
        VariantListMapper.addVariantList(typeName, propertyKey, variants);
    }
}