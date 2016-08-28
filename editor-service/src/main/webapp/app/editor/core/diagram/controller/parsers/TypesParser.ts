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

/// <reference path="../../model/Map.ts" />
/// <reference path="../../model/NodeType.ts" />
/// <reference path="../../model/PaletteTypes.ts" />
/// <reference path="../../model/ElementTypes.ts" />
/// <reference path="../../../constants/GeneralConstants.d.ts" />
/// <reference path="../../../vendor.d.ts" />

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
            var typeName: string = typeObject.type;

            elementsTypesMap[typeName] = this.createNodeType(typeObject);
        }
        return elementsTypesMap;
    }

    private parseGeneralTypes(generalTypes: any): Map<NodeType> {
        var generalTypesMap: Map<NodeType> = {};

        for (var i in generalTypes) {
            var typeObject = generalTypes[i];
            var typeName: string = typeObject.type;
            generalTypesMap[typeName] = this.createNodeType(typeObject);
        }

        return generalTypesMap;
    }

    private parsePaletteTypes(paletteTypes: any): PaletteTypes {
        var paletteTypesObject: PaletteTypes = new PaletteTypes();

        for (var category in paletteTypes) {
            var categoryTypesMap: Map<NodeType> = {};
            for (var i in paletteTypes[category]) {
                var typeObject = paletteTypes[category][i];
                var typeName: string = typeObject.type;
                categoryTypesMap[typeName] = this.createNodeType(typeObject);
            }
            paletteTypesObject.categories[category] = categoryTypesMap;
        }

        return paletteTypesObject;
    }

    private createNodeType(typeObject: any): NodeType {
        var name: string = typeObject.name;
        var typeName: string = typeObject.type;

        var elementTypeProperties: Map<Property> = this.parseTypeProperties(typeName, typeObject.properties);

        var imageElement: any = typeObject.image;

        if (imageElement) {
            var image: string = GeneralConstants.APP_ROOT_PATH + imageElement.src;
            return new NodeType(name, elementTypeProperties, image);
        } else {
            return new NodeType(name, elementTypeProperties);
        }
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