/*
 * Copyright Vladimir Zakharov
 * Copyright Anastasia Kornilova
 * Copyright Lidiya Chernigovskaya
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

class RobotsDiagramExporter extends  DiagramExporter {

    public exportSavingDiagramStateToJSON(graph: joint.dia.Graph, diagramParts: DiagramParts,
                                          name: string, folderId: number) {
        var json = {
            'diagram': {
                'name': name,
                'nodes': [],
                'links': []
            },
            'folderId': folderId
        };

        json.diagram.nodes = this.exportNodes(graph, diagramParts);
        json.diagram.links = this.exportLinks(diagramParts);

        return json;
    }

    public exportUpdatingDiagramStateToJSON(graph: joint.dia.Graph, diagramParts: DiagramParts,
                                            name: string, parentFolder: Folder) {
        var json = {
            'diagramId': parentFolder.getDiagramIdByName(name),
            'name': name,
            'nodes': [],
            'links': []
        }

        json.nodes = this.exportNodes(graph, diagramParts);
        json.links = this.exportLinks(diagramParts);

        return json;
    }

    protected exportNodes(graph: joint.dia.Graph, diagramParts: DiagramParts) {
        var nodes = [];

        for (var id in diagramParts.nodesMap) {
            var node: DiagramNode = diagramParts.nodesMap[id];
            var nodeJSON = {
                'logicalId': node.getLogicalId(),
                'graphicalId': node.getJointObject().id,
                'type': node.getType(),
                'properties': []
            };

            nodeJSON.properties = this.exportProperties(node.getChangeableProperties());

            nodeJSON.properties.push(
                {
                    "name": "name",
                    "value": node.getName(),
                    "type": "string"
                }
            );

            nodeJSON.properties.push(
                {
                    "name": "position",
                    "value": "" + node.getX() + ", " + node.getY(),
                    "type": "QPointF"
                }
            );

            nodes.push(nodeJSON);
        }

        return nodes;
    }

    protected exportLinks(diagramParts: DiagramParts) {
        var links = [];

        for (var id in diagramParts.linksMap) {
            var link: Link = diagramParts.linksMap[id];
            var jointObject = link.getJointObject();
            var linkJSON = {
                'logicalId': link.getLogicalId(),
                'graphicalId': jointObject.id,
                'type': link.getType(),
                'properties': []
            };

            linkJSON.properties.push(
                {
                    "name": "name",
                    "value": link.getName(),
                    "type": "string"
                }
            );

            linkJSON.properties = this.exportProperties(link.getChangeableProperties());

            var sourceObject = diagramParts.nodesMap[jointObject.get('source').id];
            var targetObject = diagramParts.nodesMap[jointObject.get('target').id];

            var graphicalSourceValue: string = (sourceObject) ? sourceObject.getType() +
            "/{" + jointObject.get('source').id + "}" :
                "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID";

            var source = {
                'name': "from",
                'value': "qrm:/RobotsMetamodel/RobotsDiagram/" + graphicalSourceValue,
                'type': "qReal::Id"
            }

            var graphicalTargetValue: string = (targetObject) ? targetObject.getType() +
            "/{" + jointObject.get('target').id + "}" :
                "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID";

            var target = {
                'name': "to",
                'value': "qrm:/RobotsMetamodel/RobotsDiagram/" + graphicalTargetValue,
                'type': "qReal::Id"
            }

            linkJSON.properties.push(source);
            linkJSON.properties.push(target);

            linkJSON.properties.push(
                {
                    "name": "configuration",
                    "value": this.exportVertices(jointObject),
                    "type": "QPolygon"
                }
            );

            links.push(linkJSON);
        }

        return links;
    }

}