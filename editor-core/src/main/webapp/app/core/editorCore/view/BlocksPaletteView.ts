/// <reference path="HtmlView.ts" />
/// <reference path="CategoryView.ts" />
/// <reference path="../../../core/editorCore/model/Map.ts" />
/// <reference path="../../../core/editorCore/model/NodeType.ts" />

class BlocksPaletteView extends HtmlView {

    constructor(paletteTypes: PaletteTypes) {
        super();
        var categories: Map<Map<NodeType>> = paletteTypes.categories;
        for (var categoryName in categories) {
            var category: Map<NodeType> = categories[categoryName];
            var categoryView = new CategoryView(categoryName, category);
            this.content += categoryView.getContent();
        }
    }

}