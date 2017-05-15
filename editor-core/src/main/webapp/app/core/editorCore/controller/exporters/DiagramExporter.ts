import {Property} from "../../model/Property";
import {Link} from "../../model/Link";
import {DiagramParts} from "../../model/DiagramParts";
import {SubprogramNode} from "../../model/SubprogramNode";
import {DiagramNode} from "../../model/DiagramNode";
export class DiagramExporter {

    public exportDiagramStateToJSON(graph: joint.dia.Graph, diagramParts: DiagramParts) {
        var json = {
            'nodes': [],
            'links': []
        };

        json.nodes = json.nodes.concat(this.exportNodes(graph, diagramParts));
        json.links = this.exportLinks(diagramParts);

        return json;
    }

    protected exportNodes(graph: joint.dia.Graph, diagramParts: DiagramParts) {
        var nodes = [];

        for (var node of diagramParts.nodesMap.values()) {
            var nodeJSON = {
                'logicalId': node.getLogicalId(),
                'graphicalId': node.getJointObject().id,
                'parent': node.getJointObject().get('parent'),
                'type': node.getType(),
                'logicalChildren': [],
                'graphicalChildren': [],
                'logicalLinksIds': [],
                'graphicalLinksIds': [],
                'logicalProperties': [],
                'graphicalProperties': [],
                'incomingExplosions': []
            };

            var constLogicalProperties: Map<string, Property> = node.getConstPropertiesPack().logical;
            if (node.getType() === "Subprogram") {
                constLogicalProperties.set("outgoingExplosion", new Property("outgoingExplosion", "qReal::Id",
                    "qrm:/RobotsMetamodel/RobotsDiagram/SubprogramDiagram/{" +
                    (<SubprogramNode> node).getSubprogramDiagramId() + "}"));
            }

            var changeableLogicalPropertiesArray = this.exportProperties(node.getChangeableProperties());
            var constLogicalPropertiesArray = this.exportProperties(constLogicalProperties);
            nodeJSON.logicalProperties = changeableLogicalPropertiesArray.concat(constLogicalPropertiesArray);

            nodeJSON.graphicalProperties = this.exportProperties(node.getConstPropertiesPack().graphical);

            nodeJSON.graphicalProperties.push(
                {
                    "name": "position",
                    "value": "" + node.getX() + ", " + node.getY(),
                    "type": "QPointF"
                }
            );

            var graphicalLinks = graph.getConnectedLinks(node.getJointObject(), { inbound: true, outbound: true });

            graphicalLinks.forEach(function (link) {
                nodeJSON.graphicalLinksIds.push({'id': link.id});
                nodeJSON.logicalLinksIds.push({'id': diagramParts.linksMap.get(link.id).getLogicalId()});
            });

            nodes.push(nodeJSON);
        }

        return nodes;
    }

    protected exportLinks(diagramParts: DiagramParts) {
        var links = [];

        for (var link of diagramParts.linksMap.values()) {
            var jointObject = link.getJointObject();
            if (jointObject.get('vertices')) {
                var vertices = this.exportVertices(jointObject);
            }
            var linkJSON = {
                'logicalId': link.getLogicalId(),
                'graphicalId': jointObject.id,
                'type': link.getType(),
                'logicalChildren': [],
                'graphicalChildren': [],
                'logicalLinksIds': [],
                'graphicalLinksIds': [],
                'logicalProperties': [],
                'graphicalProperties': [],
                'incomingExplosions': []
            };

            var changeableLogicalProperties = this.exportProperties(link.getChangeableProperties());
            var constLogicalProperties = this.exportProperties(link.getConstPropertiesPack().logical);
            linkJSON.logicalProperties = changeableLogicalProperties.concat(constLogicalProperties);

            linkJSON.graphicalProperties = this.exportProperties(link.getConstPropertiesPack().graphical);

            var sourceObject = diagramParts.nodesMap.get(jointObject.get('source').id);
            var targetObject = diagramParts.nodesMap.get(jointObject.get('target').id);

            var logicalSourceValue: string =  (sourceObject) ? sourceObject.getType() +
                "/{" + sourceObject.getLogicalId() + "}" :
                "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID";

            var logicalSource = {
                'name': "from",
                'value': "qrm:/RobotsMetamodel/RobotsDiagram/" + logicalSourceValue,
                'type': "qReal::Id"
            };

            var logicalTargetValue: string =  (targetObject) ? targetObject.getType() +
                "/{" + targetObject.getLogicalId() + "}" :
                "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID";

            var logicalTarget = {
                'name': "to",
                'value': "qrm:/RobotsMetamodel/RobotsDiagram/" + logicalTargetValue,
                'type': "qReal::Id"
            };

            linkJSON.logicalProperties.push(logicalSource);
            linkJSON.logicalProperties.push(logicalTarget);

            var graphicalSourceValue: string = (sourceObject) ? sourceObject.getType() +
                "/{" + jointObject.get('source').id + "}" :
                "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID";

            var graphicalSource = {
                'name': "from",
                'value': "qrm:/RobotsMetamodel/RobotsDiagram/" + graphicalSourceValue,
                'type': "qReal::Id"
            };

            var graphicalTargetValue: string = (targetObject) ? targetObject.getType() +
                "/{" + jointObject.get('target').id + "}" :
                "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID";

            var graphicalTarget = {
                'name': "to",
                'value': "qrm:/RobotsMetamodel/RobotsDiagram/" + graphicalTargetValue,
                'type': "qReal::Id"
            };

            linkJSON.graphicalProperties.push(graphicalSource);
            linkJSON.graphicalProperties.push(graphicalTarget);

            links.push(linkJSON);
        }

        return links;
    }

    protected exportProperties(properties: Map<string, Property>) {
        var propertiesJSON = [];
        for (var [propertyName, property] of properties) {
            var type: string = property.type;
            type = (type === "string" || type === "combobox" || type == "checkbox" || type == "dropdown") ?
                "QString" : type;
            var property = {
                'name': propertyName,
                'value': properties.get(propertyName).value,
                'type': type
            };
            propertiesJSON.push(property);
        }

        return propertiesJSON;
    }

    protected exportVertices(jointObject): string {
        var vertices = jointObject.get('vertices');
        var verticesStr: string = (jointObject.get('source').id) ? "0, 0 : " :
            "" + jointObject.get('source').x + ", " + jointObject.get('source').y + " : ";
        if (vertices) {
            vertices.forEach(function (vertex) {
                verticesStr += vertex.x + ", " + vertex.y + " : ";
            });
        }
        return verticesStr + (jointObject.get('target').id ? "0, 0 : " :
            "" + jointObject.get('target').x + ", " + jointObject.get('target').y + " : ");
    }
}
