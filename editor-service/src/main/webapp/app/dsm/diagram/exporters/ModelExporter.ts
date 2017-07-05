/// <reference path="../../../../resources/thrift/editor/PaletteService_types.d.ts" />
/// <reference path="../../../../resources/thrift/editor/PaletteServiceThrift.d.ts" />
/// <reference path="../../../../resources/thrift/struct/Palette_types.d.ts" />
/// <reference path="../../../types/thrift/Thrift.d.ts" />
import {Property} from "core/editorCore/model/Property";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";

export class ModelExporter {
    public exportModel(nodesMap: Map<String, DiagramNode>, linksMap: Map<String, Link>, name: string,
                       metamodelName: string) {
        var newModel = new TModel();
        newModel.name = name;
        newModel.metamodelName = metamodelName;
        newModel.nodes = this.exportNodes(nodesMap, linksMap);

        return newModel;
    }

    protected exportNodes(nodesMap: Map<String, DiagramNode>, linksMap: Map<String, Link>) {
        var nodes = [];

        var initial: string;
        for (var i in nodesMap) {
            var target: boolean = false;
            var node: DiagramNode = nodesMap[i];
            for (var id in linksMap) {
                var link = linksMap[id];
                var jointObject = link.getJointObject();
                if (nodesMap[jointObject.get('target').id] === node) {
                    target = true;
                    break;
                }
            }
            if (target === false) {
                initial = i;
                break;
            }
        }

        var existNext: boolean = true;
        while(existNext) {
            existNext = false;
            var node: DiagramNode = nodesMap[initial];
            var newNode = new TNode();
            newNode.name = node.getName();
            newNode.properties = this.exportProperties(node.getChangeableProperties());
            for (var id in linksMap) {
                var link = linksMap[id];
                var jointObject = link.getJointObject();
                if (nodesMap[jointObject.get('source').id] === node) {
                    initial = jointObject.get('target').id;
                    existNext = true;
                }
            }
            nodes.push(newNode);
        }

        return nodes;
    }

    protected exportProperties(properties: Map<String, Property>) {
        var newProperties = [];
        for (var propertyName in properties) {
            var type: string = properties[propertyName].type;
            var newProperty = new TNodeProperty();
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