/// <reference path="../../../../resources/thrift/editor/EditorService_types.d.ts" />
/// <reference path="../../../../resources/thrift/struct/Diagram_types.d.ts" />
/// <reference path="../../../../resources/thrift/editor/EditorServiceThrift.d.ts" />
/// <reference path="../../../types/thrift/Thrift.d.ts" />
import {Folder} from "../model/Folder";
import {Property} from "core/editorCore/model/Property";
import {Map} from "core/editorCore/model/Map";
import {Link} from "core/editorCore/model/Link";
import {DiagramParts} from "core/editorCore/model/DiagramParts";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {DiagramExporter} from "core/editorCore/controller/exporters/DiagramExporter";
export class DiagramThriftExporter extends DiagramExporter {
    public exportSavingDiagramState(graph: joint.dia.Graph, diagramParts: DiagramParts,
                                          name: string, folderId: number) {
        var newDiagram = new TDiagram();
        newDiagram.name = name;
        newDiagram.folderId = folderId;

        newDiagram.nodes = this.exportNodes(graph, diagramParts);
        newDiagram.links = this.exportLinks(diagramParts);

        return newDiagram;
    }

    public exportUpdatingDiagramState(graph: joint.dia.Graph, diagramParts: DiagramParts,
                                            name: string, parentFolder: Folder) {
        var newDiagram = new TDiagram();
        newDiagram.name = name;
        newDiagram.folderId = parentFolder.getId();
        newDiagram.id = parentFolder.getDiagramIdByName(name);

        newDiagram.nodes = this.exportNodes(graph, diagramParts);
        newDiagram.links = this.exportLinks(diagramParts);

        return newDiagram;
    }

    protected exportNodes(graph: joint.dia.Graph, diagramParts: DiagramParts) {
        var nodes = [];

        for (var id in diagramParts.nodesMap) {
            var node: DiagramNode = diagramParts.nodesMap[id];
            var newNode = new TDefaultDiagramNode();
            newNode.logicalId = node.getLogicalId();
            newNode.graphicalId = node.getJointObject().id;
            newNode.type = node.getType();
            newNode.properties = this.exportProperties(node.getChangeableProperties());

            var nameProperty = new TProperty();
            nameProperty.name = "name";
            nameProperty.value = node.getName();
            nameProperty.type = "string";
            newNode.properties.push(nameProperty);

            var positionProperty = new TProperty();
            positionProperty.name = "position";
            positionProperty.value = "" + node.getX() + ", " + node.getY();
            positionProperty.type = "QPointF";
            newNode.properties.push(positionProperty);

            nodes.push(newNode);
        }

        return nodes;
    }

    protected exportLinks(diagramParts: DiagramParts) {
        var links = [];

        for (var id in diagramParts.linksMap) {
            var link: Link = diagramParts.linksMap[id];
            var jointObject = link.getJointObject();
            var newLink = new TLink();
            newLink.logicalId = link.getLogicalId();
            newLink.graphicalId = jointObject.id;
            newLink.type = link.getType();
            newLink.properties = this.exportProperties(link.getChangeableProperties());

            var nameProperty = new TProperty();
            nameProperty.name = "name";
            nameProperty.value = link.getName();
            nameProperty.type = "string";
            newLink.properties.push(nameProperty);

            var sourceObject = diagramParts.nodesMap[jointObject.get('source').id];
            var targetObject = diagramParts.nodesMap[jointObject.get('target').id];

            var graphicalSourceValue: string = (sourceObject) ? sourceObject.getType() +
            "/{" + jointObject.get('source').id + "}" :
                "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID";

            var sourceProperty = new TProperty();
            sourceProperty.name = "from";
            sourceProperty.value = "qrm:/RobotsMetamodel/RobotsDiagram/" + graphicalSourceValue;
            sourceProperty.type = "qReal::Id";

            var graphicalTargetValue: string = (targetObject) ? targetObject.getType() +
            "/{" + jointObject.get('target').id + "}" :
                "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID";

            var targetProperty = new TProperty();
            targetProperty.name = "to";
            targetProperty.value = "qrm:/RobotsMetamodel/RobotsDiagram/" + graphicalTargetValue;
            targetProperty.type = "qReal::Id";

            newLink.properties.push(sourceProperty);
            newLink.properties.push(targetProperty);

            var configProperty = new TProperty();
            configProperty.name = "configuration";
            configProperty.value = this.exportVertices(jointObject);
            configProperty.type = "QPolygon";
            newLink.properties.push(configProperty);

            links.push(newLink);
        }

        return links;
    }

    protected exportProperties(properties: Map<Property>) {
        var newProperties = [];
        for (var propertyName in properties) {
            var type: string = properties[propertyName].type;
            var newProperty = new TProperty();
            type = (type === "string" || type === "combobox" || type == "checkbox" || type == "dropdown") ?
                "QString" : type;
            newProperty.name = propertyName;
            newProperty.value = properties[propertyName].value;
            newProperty.type = type;
            newProperties.push(newProperty);
        }
        return newProperties;
    }
}