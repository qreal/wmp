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

    constructor(categoryName: string, category: any) {
        super();
        var elementsContent: string = '';
        if (category instanceof PaletteSubtypes)
            category = category.categories;
        if (category instanceof Array) {
            for (var i in category) {
                var nodeType: NodeType = category[i];
                var paletteElementView: PaletteElementView = new PaletteElementView(nodeType.getName(),
                    nodeType.getShownName(), nodeType.getImage());
                elementsContent += paletteElementView.getContent();
            }

        } else {
            for (var subcategory in category) {
                var subcategoryView: CategoryView = new CategoryView(subcategory, category[subcategory]);
                elementsContent += subcategoryView.getContent();
            }
        }
        this.content = StringUtils.format(this.template, categoryName, elementsContent);
    }

}