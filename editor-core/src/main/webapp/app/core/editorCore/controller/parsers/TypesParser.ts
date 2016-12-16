/// <reference path="../../model/Map.ts" />
/// <reference path="../../model/NodeType.ts" />
/// <reference path="../../model/PaletteTypes.ts" />
/// <reference path="../../model/ElementTypes.ts" />
/// <reference path="../../../../common/constants/GeneralConstants.d.ts" />
/// <reference path="../../../../vendor.d.ts" />

class TypesParser {

    public parse(typesJson: any): ElementTypes {
        var diagramElementTypes: ElementTypes = new ElementTypes();
        var elementsTypesMap: Map<NodeType> = this.parseElementsTypes(typesJson.elements);
        var generalTypesMap: Map<NodeType> = this.parseGeneralTypes(typesJson.blocks.general);
        diagramElementTypes.uncategorisedTypes = $.extend(elementsTypesMap, generalTypesMap);
        diagramElementTypes.paletteTypes = this.parsePaletteTypes(typesJson.blocks.palette);
        return diagramElementTypes;
    }

    private parseElementsTypes(elementsTypes: any): Map<NodeType> {
        var elementsTypesMap: Map<NodeType> = {};

        for (var i in elementsTypes) {
            var typeObject = elementsTypes[i];
            $.extend(elementsTypesMap, this.createNodeTypes(typeObject).convertToMap());
        }
        return elementsTypesMap;
    }

    private parseGeneralTypes(generalTypes: any): Map<NodeType> {
        var generalTypesMap: Map<NodeType> = {};

        for (var i in generalTypes) {
            var typeObject = generalTypes[i];
            $.extend(generalTypesMap, this.createNodeTypes(typeObject).convertToMap());
        }

        return generalTypesMap;
    }

    private parsePaletteTypes(paletteTypes: any): PaletteTypes {
        var paletteTypesObject: PaletteTypes = new PaletteTypes();

        for (var category in paletteTypes) {
            var categoryTypesMap: Map<PaletteSubtypes> = {};
            for (var i in paletteTypes[category]) {
                var typeObject = paletteTypes[category][i];
                var subtypes: PaletteSubtypes = this.createNodeTypes(typeObject);
                categoryTypesMap[typeObject.type] = subtypes;
            }
            paletteTypesObject.categories[category] = categoryTypesMap;
        }

        return paletteTypesObject;
    }

    private createNodeTypes(typeObject: any): PaletteSubtypes {
        var nodes: PaletteSubtypes = new PaletteSubtypes();
        var name: string = typeObject.name;
        var typeName: string = typeObject.type;

        var elementTypeProperties: Map<Property> = this.parseTypeProperties(typeName, typeObject.properties);

        var image: string = "";
        if (typeObject.image)
            image = GeneralConstants.APP_ROOT_PATH + typeObject.image.src;

        var categories: any = typeObject.subtypes;
        if (!categories) {
            var node: NodeType = new NodeType(typeName, elementTypeProperties, image);
            nodes.categories[node.getName()] = [node];
        }
        for (var category in categories) {
            var categoryTypesList: NodeType[] = [];
            for (var i in categories[category]) {
                var subtype: string = categories[category][i];
                var node: NodeType = null;
                node = new NodeType(category.toLowerCase() + '-' + subtype.toLowerCase() + '-' +
                    name.toLowerCase(), elementTypeProperties, image, subtype);
                categoryTypesList[i] = node;
            }
            nodes.categories[category] = categoryTypesList;
        }
        return nodes;
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