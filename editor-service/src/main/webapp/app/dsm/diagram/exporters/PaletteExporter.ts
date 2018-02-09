import {GeneralConstants} from "../../../common/constants/GeneralConstants";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Link} from "core/editorCore/model/Link";

export class PaletteExporter {
    public exportPalette(nodesMap: Map<String, DiagramNode>, linksMap: Map<String, Link>, name: string) {
        var newPalette = new TPalette();

        newPalette.name = name;
        newPalette.nodes = this.getEntities(nodesMap, linksMap);

        return newPalette;
    }

    private getEntities(nodesMap: Map<String, DiagramNode>, linksMap: Map<String, Link>) {
        var nodes = [];
        for (var id in nodesMap) {
            var node: DiagramNode = nodesMap[id];
            if (node.getName() === "entity") {
                var newNode = new TNode();
                newNode.name = node.getChangeableProperties()['name'].value;
                newNode.image = GeneralConstants.APP_ROOT_PATH + "images/" + node.getChangeableProperties()['image'].value + ".svg";
                newNode.properties = this.getProperties(nodesMap, linksMap, node);

                nodes.push(newNode);
            }
        }
        return nodes;
    }

    private getProperties(nodesMap: Map<String, DiagramNode>, linksMap: Map<String, Link>, node: DiagramNode) {
        var properties = [];
        for (var id in linksMap) {
            var link = linksMap[id];
            var jointObject = link.getJointObject();
            if (nodesMap[jointObject.get('target').id] === node) {
                var property = nodesMap[jointObject.get('source').id];
                var newProperty = new TNodeProperty();
                newProperty.name = property.getChangeableProperties()['name'].value;
                newProperty.value = property.getChangeableProperties()['value'].value;
                newProperty.type = property.getChangeableProperties()['type'].value

                properties.push(newProperty);
            }
        }
        return properties;
    }

}