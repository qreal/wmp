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

/// <reference path="../../model/RobotsDiagramNode.ts" />
/// <reference path="../../model/SubprogramDiagramNode.ts" />
/// <reference path="../../model/DiagramNode.ts" />
/// <reference path="../../model/Link.ts" />
/// <reference path="../../model/DiagramParts.ts" />
/// <reference path="../../model/NodeType.ts" />
/// <reference path="../../model/Map.ts" />
/// <reference path="../../model/Property.ts" />
/// <reference path="../../../utils/MathUtils.ts" />
/// <reference path="../../../vendor.d.ts" />

class DiagramJsonParser {

    public parse(diagramJson: any, nodeTypesMap: Map<NodeType>): DiagramParts {
        var minPos: {x: number; y: number} = this.findMinPosition(diagramJson, nodeTypesMap);
        var minOffset: number = 25;
        var offsetX = (minPos.x < 0) ? (-minPos.x + minOffset) : minOffset;
        var offsetY = (minPos.y < 0) ? (-minPos.y + minOffset) : minOffset;
        var diagramParts: DiagramParts = this.parseNodes(diagramJson, nodeTypesMap, offsetX, offsetY);
        diagramParts.linksMap = this.parseLinks(diagramJson, offsetX, offsetY);
        return diagramParts;
    }

    protected findMinPosition(diagramJson: any, nodeTypesMap: Map<NodeType>): {x: number; y: number} {
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

    protected parseNodes(diagramJson: any, nodeTypesMap: Map<NodeType>, offsetX: number, offsetY: number): DiagramParts {
        var diagramParts: DiagramParts = new DiagramParts();

        for (var i = 0; i < diagramJson.nodes.length; i++) {
            var nodeObject = diagramJson.nodes[i];
            var type = nodeObject.type;

            if (type === "RobotsDiagramNode") {
                diagramParts.robotsDiagramNode = this.parseRobotsDiagramNode(nodeObject);
            } else if (type === "SubprogramDiagram") {
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

    protected parseRobotsDiagramNode(nodeObject: any): RobotsDiagramNode {
        var logicalProperties: Map<Property> = {};
        var logicalPropertiesObject = nodeObject.logicalProperties;
        for (var i = 0; i < logicalPropertiesObject.length; i++) {
            var propertyName = logicalPropertiesObject[i].name;
            if (propertyName === "devicesConfiguration" || propertyName === "worldModel") {
                var property:Property = new Property(propertyName, logicalPropertiesObject[i].type,
                    logicalPropertiesObject[i].value);
                logicalProperties[propertyName] = property;
            }
        }

        return new RobotsDiagramNode(nodeObject.logicalId, nodeObject.graphicalId, logicalProperties);
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

    protected parseDiagramNodeObject(nodeObject: any, nodeTypesMap: Map<NodeType>,
                                   offsetX: number, offsetY: number): DiagramNode {
        var changeableLogicalProperties: Map<Property> = {};
        var constLogicalProperties: Map<Property> = {};
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

        var constGraphicalProperties: Map<Property> = {};
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
            node = new SubprogramNode(name, type, x, y, changeableLogicalProperties,
                nodeTypesMap[nodeObject.type].getImage(),
                subprogramDiagramId, nodeObject.graphicalId,
                new PropertiesPack(constLogicalProperties, constGraphicalProperties));
        } else {
            node = new DefaultDiagramNode(name, type, x, y, changeableLogicalProperties,
                nodeTypesMap[nodeObject.type].getImage(), nodeObject.graphicalId,
                new PropertiesPack(constLogicalProperties, constGraphicalProperties));
        }

        return node;
    }

    protected parseLinks(diagramJson: any, offsetX: number, offsetY: number): Map<Link> {
        var linksMap: Map<Link> = {};

        for (var i = 0; i < diagramJson.links.length; i++) {
            linksMap[diagramJson.links[i].graphicalId] = this.parseLinkObject(diagramJson.links[i], offsetX, offsetY);
        }

        return linksMap;
    }

    protected parseLinkObject(linkObject: any, offsetX: number, offsetY: number): Link {
        var sourceId: string = "";
        var targetId: string = "";

        var properties: Map<Property> = {};
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
                    break
                case "position":
                    linkPosition = this.parsePosition(graphicalPropertiesObject[j].value);
                    break
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

        var jointObject: joint.dia.Link = new joint.dia.Link({
            id: jointObjectId,
            attrs: {
                '.connection': { stroke: 'black' },
                '.marker-target': { fill: 'black', d: 'M 10 0 L 0 5 L 10 10 z' }
            },
            source: sourceObject,
            target: targetObject,
            vertices: vertices
        });

        return new Link(jointObject, properties);
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

    protected parseId(idString: string): string {
        var parts = idString.split("/");
        var id = parts[parts.length - 1];
        var expr = /{(.*)}/gi;
        return id.replace(expr, "$1");
    }

}