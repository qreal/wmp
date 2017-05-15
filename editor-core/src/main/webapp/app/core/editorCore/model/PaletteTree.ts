import {NodeType} from "./NodeType";
import {MapUtils} from "../../../utils/MapUtils";
export class PaletteTree {

    categories: Map<string, PaletteTree>;
    nodes: NodeType[];

    constructor() {
        this.categories = new Map<string, PaletteTree>();
        this.nodes = [];
    }

    convertToMap(): Map<string, NodeType> {
        var nodesMap: Map<string, NodeType> = new Map<string, NodeType>();
        for (var category of this.categories.values())
            MapUtils.extend(nodesMap, category.convertToMap());
        for (var i in this.nodes)
            nodesMap.set(this.nodes[i].getName(), this.nodes[i]);
        return nodesMap;
    }

    convertToFullNameMap(suffix: string): Map<string, NodeType> {
        var nodesMap: Map<string, NodeType> = new Map<string, NodeType>();
        for (var [categoryName, category] of this.categories)
            MapUtils.extend(nodesMap, category.convertToFullNameMap(categoryName + " " + suffix));
        for (var i in this.nodes)
            nodesMap.set(this.nodes[i].getShownName() + " " + suffix, this.nodes[i]);
        return nodesMap;
    }
}