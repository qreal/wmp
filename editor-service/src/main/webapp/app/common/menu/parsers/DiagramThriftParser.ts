/// <reference path="../../../../resources/thrift/editor/EditorService_types.d.ts" />
/// <reference path="../../../../resources/thrift/struct/Diagram_types.d.ts" />
/// <reference path="../../../../resources/thrift/editor/EditorServiceThrift.d.ts" />
/// <reference path="../../../types/thrift/Thrift.d.ts" />
import {Property} from "core/editorCore/model/Property";
import {Link} from "core/editorCore/model/Link";
import {NodeType} from "core/editorCore/model/NodeType";
import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {DiagramParts} from "core/editorCore/model/DiagramParts";
import {DiagramJsonParser} from "core/editorCore/controller/parsers/DiagramJsonParser";
import {ElementConstructor} from "core/editorCore/model/ElementConstructor";
export class DiagramThriftParser extends DiagramJsonParser {

    private elementConstructor: ElementConstructor;

    constructor(elementConstructor: ElementConstructor) {
        super();
        this.elementConstructor = elementConstructor;
    }

    public parse(diagram: TDiagram, nodeTypesMap: Map<string, NodeType>, linkPatterns: Map<string, joint.dia.Link>): DiagramParts {
        var diagramParts: DiagramParts = this.parseNodes(diagram, nodeTypesMap, 0, 0);
        diagramParts.linksMap = this.parseLinks(diagram, nodeTypesMap, linkPatterns, 0, 0);
        this.setEmbedding(diagramParts.nodesMap, diagram.nodes);
        return diagramParts;
    }

    protected parseDiagramNodeObject(nodeObject: TDefaultDiagramNode, nodeTypesMap: Map<string, NodeType>,
                                     offsetX: number, offsetY: number): DiagramNode {
        var changeableLogicalProperties: Map<string, Property> = new Map<string, Property>();
        var name = "";
        var type = nodeObject.type;

        var propertiesObject = nodeObject.properties;

        var typeProperties = nodeTypesMap.get(type).getPropertiesMap();

        propertiesObject.sort(function (a: any, b: any) {
            if (a.name < b.name) return -1;
            if (a.name > b.name) return 1;
            return 0;
        });

        var x: number = 0;
        var y: number = 0;
        var z: number = 0;
        var width: number = 0;
        var height: number = 0;

        for (var j = 0; j < propertiesObject.length; j++) {
            var propertyName = propertiesObject[j].name;

            if (propertyName === "name") {
                name = propertiesObject[j].value;
            }

            if (typeProperties.has(propertyName)) {
                var typeProperty: Property = typeProperties.get(propertyName);
                var property: Property = new Property(typeProperty.name, typeProperty.type, propertiesObject[j].value);
                changeableLogicalProperties.set(propertyName, property);
            } else if (propertyName === "position") {
                var position: string = propertiesObject[j].value;
                var positionNums = this.parsePosition(position);
                x = positionNums.x + offsetX;
                y = positionNums.y + offsetY;
            } else if (propertyName === "size") {
                var size: string = propertiesObject[j].value;
                var bboxDimensions = this.parseSize(size);
                width = bboxDimensions.width;
                height = bboxDimensions.height;
            } else if (propertyName == "z") {
                z = parseInt(propertiesObject[j].value);
            }
        }

        var node: DiagramNode = this.elementConstructor.createNode(nodeTypesMap.get(type), x, y, width, height,
            changeableLogicalProperties, nodeObject.graphicalId);
        node.getJointObject().set("z", z);
        return node;
    }


    protected parseLinkObject(linkObject: TLink, nodeTypesMap: Map<string, NodeType>,
                              linkPatterns: Map<string, joint.dia.Link>, offsetX: number, offsetY: number): Link {
        var sourceId: string = "";
        var targetId: string = "";

        var properties: Map<string, Property> = new Map<string, Property>();
        var propertiesObject = linkObject.properties;

        var vertices = [];
        var linkPosition: {x: number; y: number};
        var configuration: string = "";

        for (var j = 0; j < propertiesObject.length; j++) {
            switch (propertiesObject[j].name) {
                case "Guard":
                    var property: Property = new Property("Guard", "combobox", propertiesObject[j].value);
                    properties.set("Guard", property);
                    break;
                case "from":
                    sourceId = this.parseId(propertiesObject[j].value);
                    break;
                case "to":
                    targetId = this.parseId(propertiesObject[j].value);
                    break;
                case "position":
                    linkPosition = this.parsePosition(propertiesObject[j].value);
                    break;
                case "configuration":
                    configuration = propertiesObject[j].value;
                    vertices = this.parseVertices(propertiesObject[j].value);
                    break;
            }
        }

        var jointObjectId = linkObject.graphicalId;

        var sourceObject;
        if (sourceId !== "ROOT_ID") {
            sourceObject = {id: sourceId};
        } else {
            sourceObject = this.getSourcePosition(configuration);
        }

        var targetObject;
        if (targetId !== "ROOT_ID") {
            targetObject = {id: targetId};
        } else {
            targetObject = this.getTargetPosition(configuration);
        }

        var jointObject: joint.dia.Link = <joint.dia.Link> linkPatterns.get(linkObject.type).clone();
        jointObject.set({
            id: jointObjectId,
            source: sourceObject,
            target: targetObject,
            vertices: vertices
        });

        var nodeType: NodeType = nodeTypesMap.get(linkObject.type);
        return this.elementConstructor.createLink(jointObject, nodeType.getShownName(), nodeType.getName(), properties);
    }

    protected setEmbedding(nodesMap: Map<string, DiagramNode>, nodeObjects: TDefaultDiagramNode[]) {
        for (var i = 0; i < nodeObjects.length; i++) {
            if (!nodeObjects[i].parentId)
                continue;
            var child: DiagramNode = nodesMap.get(nodeObjects[i].graphicalId);
            var parent: DiagramContainer = <DiagramContainer> nodesMap.get(nodeObjects[i].parentId);
            child.setParentNode(parent);
        }
    }
}