import {StringUtils} from "../../../utils/StringUtils";
import {PaletteElementView} from "./PaletteElementView";
import {NodeType} from "../model/NodeType";
import {PaletteTree} from "../model/PaletteTree";
import {HtmlView} from "./HtmlView";
import {ContainerNodeType} from "../model/ContainerNodeType";
export class CategoryView extends HtmlView {

    private template: string = '' +
        '<li>' +
        '   <p>{0}</p>' +
        '   <ul>{1}</ul>' +
        '</li>';

    constructor(categoryName: string, category: PaletteTree, elementClass: string) {
        super();
        var elementsContent: string = '';
        for (var [subcategoryName, subcategory] of category.categories) {
            var subcategoryView: CategoryView = new CategoryView(subcategoryName, subcategory, elementClass);
            elementsContent += subcategoryView.getContent();
        }
        for (var i in category.nodes) {
            var nodeType: NodeType = category.nodes[i];
            if (!nodeType.getVisibility())
                continue;
            var scale: number = nodeType instanceof ContainerNodeType ? 1.5 : 1;
            var paletteElementView: PaletteElementView = new PaletteElementView(nodeType.getName(),
                nodeType.getShownName(), nodeType.getIcon(), elementClass, scale);
            elementsContent += paletteElementView.getContent();
        }
        /*var nodeType: NodeType = MapUtils.getFirstValue(category.convertToMap());
         var scale: number = nodeType instanceof ContainerNodeType ? 1.5 : 1;
         elementsContent += new PaletteElementView(nodeType.getName(), nodeType.getShownName(), nodeType.getIcon(),
         elementClass, scale).getContent();*/
        if (elementsContent)
            this.content = StringUtils.format(this.template, categoryName, elementsContent);
    }

}