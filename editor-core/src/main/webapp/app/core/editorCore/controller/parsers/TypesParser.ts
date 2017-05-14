import {VariantListMapper} from "../VariantListMapper";
import {Variant} from "../../model/Variant";
import {Property} from "../../model/Property";
import {NodeType} from "../../model/NodeType";
import {PaletteTree} from "../../model/PaletteTree";
import {GeneralConstants} from "../../../../common/constants/GeneralConstants";
import {ElementTypes} from "../../model/ElementTypes";
import {ContainerNodeType} from "../../model/ContainerNodeType";
export class TypesParser {

    private currentProperties: any;
    private linkPatterns: Map<String, joint.dia.Link>;
    private currentImage: any;
    private currentBorder: any;
    private currentInnerText: any;

    public parse(typesJson: any): ElementTypes {
        var diagramElementTypes: ElementTypes = new ElementTypes();
        this.linkPatterns = new Map<String, joint.dia.Link>();
        diagramElementTypes.blockTypes = this.parsePaletteTypes(typesJson.blocks);
        diagramElementTypes.flowTypes = this.parseFlowTypes(typesJson.flows);
        var flowsMap: Map<String, NodeType> = diagramElementTypes.flowTypes.convertToMap();
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

    private parseFlowTypes(elementsTypes: any): PaletteTree {
        var elementsTree: PaletteTree = new PaletteTree();
        var elements: PaletteTree = new PaletteTree();
        elementsTree.categories["flows"] = elements;

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

        this.currentProperties = typeObject.properties;

        this.currentImage = typeObject.image;
        if (typeObject.image)
            this.currentImage.src = GeneralConstants.APP_ROOT_PATH + typeObject.image.src;

        this.currentBorder = typeObject.border;
        this.currentInnerText = typeObject.innerText;

        if (typeObject.attrs)
            this.linkPatterns[typeName.toLowerCase()] = new joint.dia.Link({attrs: typeObject.attrs});

        var categories: any = typeObject.subtypes;
        if (!categories) {
            var node: NodeType;
            if (typeObject.container)
                node = new ContainerNodeType(name, this.parseTypeProperties(typeName, this.currentProperties),
                    this.currentImage, this.currentBorder, this.currentInnerText, typeName);
            else
                node = new NodeType(name, this.parseTypeProperties(typeName, typeObject.properties),
                    this.currentImage, this.currentBorder, this.currentInnerText, typeName);
            nodesTree = new PaletteTree();
            nodesTree.nodes.push(node);
        } else {
            nodesTree = this.parseSubtypes(categories, [typeName]);
        }

        for (var i in typeObject.properties) {
            if (typeObject.properties[i].type === "changeType")
                this.addChangeTypeProperty(nodesTree);
        }

        return nodesTree;
    }

    private parseSubtypes(categories: any, path: string[]): PaletteTree {
        var nodesTree: PaletteTree = new PaletteTree();
        if (categories instanceof Array) {
            for (var i in categories) {
                var subtype: string = categories[i];
                path.push(subtype.toLowerCase());
                var typeName: string = path.reverse().join('-');
                nodesTree.nodes.push(new NodeType(subtype, this.parseTypeProperties(typeName, this.currentProperties),
                    this.currentImage, this.currentBorder, this.currentInnerText, typeName));
                path.reverse();
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

    private addChangeTypeProperty(tree: PaletteTree) {
        var nodeTypes: Map<String, NodeType> = tree.convertToMap();
        var nodeTypesArrayNode = [];
        for (var key in nodeTypes) {
            nodeTypesArrayNode.push({"key": nodeTypes[key].getName(), "value": nodeTypes[key].getName()});
        }
        for (var key in nodeTypes) {
            this.addVariantList(nodeTypes[key].getName(), "ChangeType", nodeTypesArrayNode);
        }
    }

    private parseTypeProperties(typeName: string, propertiesArrayNode: any): Map<String, Property> {
        var properties: Map<String, Property> = new Map<String, Property>()
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
            } else if (propertyType === "changeType") {
                propertyValue = typeName;
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
