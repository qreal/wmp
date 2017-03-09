/// <reference path="HtmlView.ts" />
/// <reference path="PaletteElementView.ts" />
/// <reference path="../../../core/editorCore/model/Map.ts" />
/// <reference path="../../../core/editorCore/model/NodeType.ts" />
/// <reference path="../../../utils/StringUtils.ts" />

class CategoryView extends HtmlView {

    private template: string = '' +
        '<li>' +
        '   <p>{0}</p>' +
        '   <ul>{1}</ul>' +
        '</li>';

    constructor(categoryName: string, category: PaletteTree, elementClass: string) {
        super();
        var elementsContent: string = '';
        for (var subcategory in category.categories) {
            var subcategoryView: CategoryView = new CategoryView(subcategory, category.categories[subcategory], elementClass);
            elementsContent += subcategoryView.getContent();
        }
        for (var i in category.nodes) {
            var nodeType: NodeType = category.nodes[i];
            if (!nodeType.getVisibility())
                continue;
            var paletteElementView: PaletteElementView = new PaletteElementView(nodeType.getName(),
                nodeType.getShownName(), nodeType.getImage(), elementClass);
            elementsContent += paletteElementView.getContent();
        }
        if (elementsContent)
            this.content = StringUtils.format(this.template, categoryName, elementsContent);
    }

}