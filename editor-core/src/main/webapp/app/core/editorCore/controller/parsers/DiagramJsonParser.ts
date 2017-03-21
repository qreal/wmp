import {Link} from "../../model/Link";
import {NodeType} from "../../model/NodeType";
import {Property} from "../../model/Property";
import {PropertiesPack} from "../../model/PropertiesPack";
import {DefaultDiagramNode} from "../../model/DefaultDiagramNode";
import {SubprogramNode} from "../../model/SubprogramNode";
import {DiagramNode} from "../../model/DiagramNode";
import {SubprogramDiagramNode} from "../../model/SubprogramDiagramNode";
import {DiagramParts} from "../../model/DiagramParts";
import {MathUtils} from "../../../../utils/MathUtils";
import {UIDGenerator} from "../UIDGenerator";
import {DefaultSize} from "../../../../common/constants/DefaultSize";

export class DiagramJsonParser {

    public parse(diagramJson: any, nodeTypesMap: Map<String, NodeType>, linkPatterns: Map<String, joint.dia.Link>): DiagramParts {
        var minPos: {x: number; y: number} = this.findMinPosition(diagramJson, nodeTypesMap);
        var minOffset: number = 25;
        var offsetX = (minPos.x < 0) ? (-minPos.x + minOffset) : minOffset;
        var offsetY = (minPos.y < 0) ? (-minPos.y + minOffset) : minOffset;
        var diagramParts: DiagramParts = this.parseNodes(diagramJson, nodeTypesMap, offsetX, offsetY);
        diagramParts.linksMap = this.parseLinks(diagramJson, nodeTypesMap, linkPatterns, offsetX, offsetY);
        return diagramParts;
    }

    protected findMinPosition(diagramJson: any, nodeTypesMap: Map<String, NodeType>): {x: number; y: number} {
        var minX = Infinity;
        var minY = Infinity;

        for (var i = 0; i < diagramJson.nodes.length; i++) {
            var nodeObject = diagramJson.nodes[i];
            var type = nodeObject.type;

            if (nodeTypesMap[type]) {
                var graphicalPropertiesObject = nodeObject.graphicalProperties;

                var x: number = 0;
                var y: number = 0;
                for (var j = 0; j < graphicalPropertiesObject.length; j++) {
                    if (graphicalPropertiesObject[j].name === "position") {
                        var position:string = graphicalPropertiesObject[j].value;
                        var parts = position.split(", ");
                        x = parseFloat(parts[0]);
                        y = parseFloat(parts[1]);

                        minX = MathUtils.min(x, minX);
                        minY = MathUtils.min(y, minY);
                    }
                }
            }
        }

        return {x: minX, y: minY};
    }

    protected parseNodes(diagramJson: any, nodeTypesMap: Map<String, NodeType>, offsetX: number, offsetY: number): DiagramParts {
        var diagramParts: DiagramParts = new DiagramParts();

        for (var i = 0; i < diagramJson.nodes.length; i++) {
            var nodeObject = diagramJson.nodes[i];
            var type = nodeObject.type;

            if (type === "SubprogramDiagram") {
                diagramParts.subprogramDiagramNodes.push(this.parseSubprogramDiagram(nodeObject));
            } else {
                if (nodeTypesMap[type]) {
                    diagramParts.nodesMap[nodeObject.graphicalId] = this.parseDiagramNodeObject(nodeObject,
                        nodeTypesMap, offsetX, offsetY);
                }
            }
        }

        return diagramParts;
    }

    protected parseSubprogramDiagram(nodeObject: any): SubprogramDiagramNode {
        var name: string = "";
        var logicalPropertiesObject = nodeObject.logicalProperties;
        for (var i = 0; i < logicalPropertiesObject.length; i++) {
            var propertyName = logicalPropertiesObject[i].name;
            if (propertyName === "name") {
                name = logicalPropertiesObject[i].value;
            }
        }

        return new SubprogramDiagramNode(nodeObject.logicalId, name);
    }

    protected parseDiagramNodeObject(nodeObject: any, nodeTypesMap: Map<String, NodeType>,
                                     offsetX: number, offsetY: number): DiagramNode {
        var changeableLogicalProperties: Map<String, Property> = new Map<String, Property>();
        var constLogicalProperties: Map<String, Property> = new Map<String, Property>();
        var subprogramDiagramId: string = "";
        var name = "";
        var type = nodeObject.type;

        var logicalPropertiesObject = nodeObject.logicalProperties;

        var typeProperties = nodeTypesMap[nodeObject.type].getPropertiesMap();

        logicalPropertiesObject.sort(function (a:any, b:any) {
            if (a.name < b.name) return -1;
            if (a.name > b.name) return 1;
            return 0;
        });

        for (var j = 0; j < logicalPropertiesObject.length; j++) {
            var propertyName = logicalPropertiesObject[j].name;

            if (propertyName === "name") {
                name  = logicalPropertiesObject[j].value;
            }
            if (propertyName === "outgoingExplosion") {
                var outgoingExplosionId = this.parseId(logicalPropertiesObject[j].value);
                if (outgoingExplosionId) {
                    subprogramDiagramId = outgoingExplosionId;
                }
            }

            if (typeProperties.hasOwnProperty(propertyName)) {
                var property: Property = new Property(typeProperties[propertyName].name,
                    typeProperties[propertyName].type, logicalPropertiesObject[j].value);
                changeableLogicalProperties[propertyName] = property;
            } else {
                var property: Property = new Property(logicalPropertiesObject[j].name,
                    logicalPropertiesObject[j].type, logicalPropertiesObject[j].value);
                constLogicalProperties[propertyName] = property;
            }
        }

        var constGraphicalProperties: Map<String, Property> = new Map<String, Property>();
        var graphicalPropertiesObject = nodeObject.graphicalProperties;

        var x: number = 0;
        var y: number = 0;
        for (var j = 0; j < graphicalPropertiesObject.length; j++) {
            var propertyName = graphicalPropertiesObject[j].name;
            if (propertyName === "position") {
                var position:string = graphicalPropertiesObject[j].value;
                var positionNums = this.parsePosition(position);
                x = positionNums.x + offsetX;
                y = positionNums.y + offsetY;
            } else {
                var property: Property = new Property(graphicalPropertiesObject[j].name,
                    graphicalPropertiesObject[j].type, graphicalPropertiesObject[j].value);
                constGraphicalProperties[propertyName] = property;
            }
        }

        var node: DiagramNode;
        if (subprogramDiagramId) {
            node = new SubprogramNode(name, type, x, y, DefaultSize.DEFAULT_NODE_WIDTH,
                DefaultSize.DEFAULT_NODE_HEIGHT, changeableLogicalProperties, nodeTypesMap[nodeObject.type].getImage(),
                subprogramDiagramId, nodeObject.graphicalId,
                new PropertiesPack(constLogicalProperties, constGraphicalProperties));
        } else {
            node = new DefaultDiagramNode(UIDGenerator.generate(), name, type, x, y, DefaultSize.DEFAULT_NODE_WIDTH,
                DefaultSize.DEFAULT_NODE_HEIGHT, changeableLogicalProperties,
                nodeTypesMap[nodeObject.type].getImage(), nodeObject.graphicalId,
                new PropertiesPack(constLogicalProperties, constGraphicalProperties));
        }

        return node;
    }

    protected parseLinks(diagramJson: any, nodeTypesMap: Map<String, NodeType>, linkPatterns: Map<String, joint.dia.Link>,
                         offsetX: number, offsetY: number): Map<String, Link> {
        var linksMap: Map<String, Link> = new Map<String, Link>();

        for (var i = 0; i < diagramJson.links.length; i++) {
            linksMap[diagramJson.links[i].graphicalId] = this.parseLinkObject(diagramJson.links[i], nodeTypesMap,
                linkPatterns, offsetX, offsetY);
        }

        return linksMap;
    }

    protected parseLinkObject(linkObject: any, nodeTypesMap: Map<String, NodeType>, linkPatterns: Map<String, joint.dia.Link>,
                              offsetX: number, offsetY: number): Link {
        var sourceId: string = "";
        var targetId: string = "";

        var properties: Map<String, Property> = new Map<String, Property>();
        var logicalPropertiesObject = linkObject.logicalProperties;

        for (var j = 0; j < logicalPropertiesObject.length; j++) {
            switch (logicalPropertiesObject[j].name) {
                case "Guard":
                    var property: Property = new Property("Guard", "combobox", logicalPropertiesObject[j].value);
                    properties["Guard"] = property;
                    break;
            }
        }

        var graphicalPropertiesObject = linkObject.graphicalProperties;
        var vertices = [];
        var linkPosition: {x: number; y: number};
        var configuration: string = "";

        for (var j = 0; j < graphicalPropertiesObject.length; j++) {
            switch (graphicalPropertiesObject[j].name) {
                case "from":
                    sourceId = this.parseId(graphicalPropertiesObject[j].value);
                    break;
                case "to":
                    targetId = this.parseId(graphicalPropertiesObject[j].value);
                    break;
                case "position":
                    linkPosition = this.parsePosition(graphicalPropertiesObject[j].value);
                    break;
                case "configuration":
                    configuration = graphicalPropertiesObject[j].value;
                    vertices = this.parseVertices(graphicalPropertiesObject[j].value);
                    break;
                default:

            }
        }

        vertices.forEach(function (vertex: {x: number; y: number}) {
            vertex.x += linkPosition.x + offsetX;
            vertex.y += linkPosition.y + offsetY;
        });

        var jointObjectId = linkObject.graphicalId;

        var sourceObject;
        if (sourceId !== "ROOT_ID") {
            sourceObject = {id: sourceId};
        } else {
            var sourcePosition = this.getSourcePosition(configuration);
            sourcePosition.x += linkPosition.x + offsetX;
            sourcePosition.y += linkPosition.y + offsetY;
            sourceObject = sourcePosition;
        }

        var targetObject;
        if (targetId !== "ROOT_ID") {
            targetObject = {id: targetId};
        } else {
            var targetPosition = this.getTargetPosition(configuration);
            targetPosition.x += linkPosition.x + offsetX;
            targetPosition.y += linkPosition.y + offsetY;
            targetObject = targetPosition;
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

    protected parseVertices(configuration: string) {
        var vertices = [];
        var parts = configuration.split(" : ");

        for (var k = 1; k < parts.length - 2; k++) {
            vertices.push(this.parsePosition(parts[k]));
        }
        return vertices;
    }

    protected getSourcePosition(configuration: string) {
        var parts = configuration.split(" : ");
        var position = this.parsePosition(parts[0]);
        position.x = Math.floor(position.x);
        position.x = position.x - position.x % 5;
        position.y = Math.floor(position.y);
        position.y = position.y - position.y % 5;
        return position;
    }

    protected getTargetPosition(configuration: string) {
        var parts = configuration.split(" : ");
        var position = this.parsePosition(parts[parts.length - 2]);
        position.x = Math.floor(position.x);
        position.x = position.x - position.x % 5;
        position.y = Math.floor(position.y);
        position.y = position.y - position.y % 5;
        return position;
    }

    protected parsePosition(position: string): {x: number; y: number} {
        var parts = position.split(", ");
        return {x: parseFloat(parts[0]), y: parseFloat(parts[1])};
    }

    protected parseSize(size: string): {width: number; height: number} {
        var parts = size.split(", ");
        return {width: parseFloat(parts[0]), height: parseFloat(parts[1])};
    }

    protected parseId(idString: string): string {
        var parts = idString.split("/");
        var id = parts[parts.length - 1];
        var expr = /{(.*)}/gi;
        return id.replace(expr, "$1");
    }

}