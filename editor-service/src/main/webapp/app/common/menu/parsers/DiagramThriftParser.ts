/// <reference path="../../../../resources/thrift/editor/EditorService_types.d.ts" />
/// <reference path="../../../../resources/thrift/struct/Diagram_types.d.ts" />
/// <reference path="../../../../resources/thrift/editor/EditorServiceThrift.d.ts" />
/// <reference path="../../../types/thrift/Thrift.d.ts" />
import {Property} from "core/editorCore/model/Property";
import {Link} from "core/editorCore/model/Link";
import {NodeType} from "core/editorCore/model/NodeType";
import {DefaultDiagramNode} from "core/editorCore/model/DefaultDiagramNode";
import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {ContainerNodeType} from "core/editorCore/model/ContainerNodeType";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {DiagramParts} from "core/editorCore/model/DiagramParts";
import {DiagramJsonParser} from "core/editorCore/controller/parsers/DiagramJsonParser";
export class DiagramThriftParser extends DiagramJsonParser {

    public parse(diagram: TDiagram, nodeTypesMap: Map<String, NodeType>, linkPatterns: Map<String, joint.dia.Link>): DiagramParts {
        var diagramParts: DiagramParts = this.parseNodes(diagram, nodeTypesMap, 0, 0);
        diagramParts.linksMap = this.parseLinks(diagram, nodeTypesMap, linkPatterns, 0, 0);
        this.setEmbedding(diagramParts.nodesMap, diagram.nodes);
        return diagramParts;
    }

    protected parseDiagramNodeObject(nodeObject: TDefaultDiagramNode, nodeTypesMap: Map<String, NodeType>,
                                     offsetX: number, offsetY: number): DiagramNode {
        var changeableLogicalProperties: Map<String, Property> = new Map<String, Property>();
        var name = "";
        var type = nodeObject.type;

        var propertiesObject = nodeObject.properties;

        var typeProperties = nodeTypesMap[nodeObject.type].getPropertiesMap();

        propertiesObject.sort(function (a:any, b:any) {
            if (a.name < b.name) return -1;
            if (a.name > b.name) return 1;
            return 0;
        });

        var x: number = 0;
        var y: number = 0;
        var width: number = 0;
        var height: number = 0;

        for (var j = 0; j < propertiesObject.length; j++) {
            var propertyName = propertiesObject[j].name;

            if (propertyName === "name") {
                name = propertiesObject[j].value;
            }

            if (typeProperties.hasOwnProperty(propertyName)) {
                var property:Property = new Property(typeProperties[propertyName].name,
                    typeProperties[propertyName].type, propertiesObject[j].value);
                changeableLogicalProperties[propertyName] = property;
            } else if (propertyName === "position") {
                var position: string = propertiesObject[j].value;
                var positionNums = this.parsePosition(position);
                x = positionNums.x + offsetX;
                y = positionNums.y + offsetY;
            } else if (propertyName === "size") {
                var size:string = propertiesObject[j].value;
                var bboxDimensions = this.parseSize(size);
                width = bboxDimensions.width;
                height = bboxDimensions.height;
            }
        }

        var node: DiagramNode;
        if (nodeTypesMap[type] instanceof ContainerNodeType)
            node = new DiagramContainer(name, type, x, y, width, height, changeableLogicalProperties,
                nodeTypesMap[nodeObject.type].getImage(), nodeObject.graphicalId);
        else
            node = new DefaultDiagramNode(name, type, x, y, width, height, changeableLogicalProperties,
                nodeTypesMap[nodeObject.type].getImage(), nodeObject.graphicalId);

        return node;
    }


    protected parseLinkObject(linkObject: TLink, nodeTypesMap: Map<String, NodeType>,
                              linkPatterns: Map<String, joint.dia.Link>, offsetX: number, offsetY: number): Link {
        var sourceId: string = "";
        var targetId: string = "";

        var properties: Map<String, Property> = new Map<String, Property>();
        var propertiesObject = linkObject.properties;

        var vertices = [];
        var linkPosition: {x: number; y: number};
        var configuration: string = "";

        for (var j = 0; j < propertiesObject.length; j++) {
            switch (propertiesObject[j].name) {
                case "Guard":
                    var property: Property = new Property("Guard", "combobox", propertiesObject[j].value);
                    properties["Guard"] = property;
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

        var jointObject: joint.dia.Link = <joint.dia.Link> linkPatterns[linkObject.type].clone();
        jointObject.set({
            id: jointObjectId,
            source: sourceObject,
            target: targetObject,
            vertices: vertices
        });

        var nodeType: NodeType = nodeTypesMap[linkObject.type];
        return new Link(jointObject, nodeType.getShownName(), nodeType.getName(), properties);
    }

    protected setEmbedding(nodesMap: Map<String, DiagramNode>, nodeObjects: TDefaultDiagramNode[]) {
        for (var i = 0; i < nodeObjects.length; i++) {
            if (!nodeObjects[i].parentId)
                continue;
            var child: DiagramNode = nodesMap[nodeObjects[i].graphicalId];
            var parent: DiagramContainer = <DiagramContainer> nodesMap[nodeObjects[i].parentId];
            parent.getJointObject().embed(child.getJointObject());
            child.setParentNode(parent);
        }
    }
}