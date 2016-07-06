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

/// <reference path="../../../diagramCore.d.ts" />
/// <reference path="../../../vendor.d.ts" />

class RobotsDiagramJsonParser extends DiagramJsonParser {

    public parse(diagramJson: any, nodeTypesMap: Map<NodeType>): DiagramParts {
        var diagramParts: DiagramParts = this.parseNodes(diagramJson, nodeTypesMap, 0, 0);
        diagramParts.linksMap = this.parseLinks(diagramJson, 0, 0);
        return diagramParts;
    }

    protected parseDiagramNodeObject(nodeObject: any, nodeTypesMap: Map<NodeType>,
                                     offsetX: number, offsetY: number): DiagramNode {
        var changeableLogicalProperties: Map<Property> = {};
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

        for (var j = 0; j < propertiesObject.length; j++) {
            var propertyName = propertiesObject[j].name;

            if (propertyName === "name") {
                name  = propertiesObject[j].value;
            }

            if (typeProperties.hasOwnProperty(propertyName)) {
                var property:Property = new Property(typeProperties[propertyName].name,
                    typeProperties[propertyName].type, propertiesObject[j].value);
                changeableLogicalProperties[propertyName] = property;
            } else if (propertyName === "position") {
                var position:string = propertiesObject[j].value;
                var positionNums = this.parsePosition(position);
                x = positionNums.x + offsetX;
                y = positionNums.y + offsetY;
            }
        }

        var node: DiagramNode = new DefaultDiagramNode(name, type, x, y, changeableLogicalProperties,
                nodeTypesMap[nodeObject.type].getImage(), nodeObject.graphicalId);

        return node;
    }


    protected parseLinkObject(linkObject: any, offsetX: number, offsetY: number): Link {
        var sourceId: string = "";
        var targetId: string = "";

        var properties: Map<Property> = {};
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
                    break
                case "position":
                    linkPosition = this.parsePosition(propertiesObject[j].value);
                    break
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
            targetObject = this.getTargetPosition(configuration);;
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

}