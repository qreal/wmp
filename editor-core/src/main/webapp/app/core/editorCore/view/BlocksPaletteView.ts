/// <reference path="HtmlView.ts" />
/// <reference path="CategoryView.ts" />
/// <reference path="../../../core/editorCore/model/Map.ts" />
/// <reference path="../../../core/editorCore/model/NodeType.ts" />

class BlocksPaletteView extends HtmlView {

    constructor(paletteTypes: PaletteTree, elementClass: string) {
        super();
        var categories: Map<PaletteTree> = paletteTypes.categories;
        for (var categoryName in categories) {
            var category: PaletteTree = categories[categoryName];
            var categoryView = new CategoryView(categoryName, category, elementClass);
            this.content += categoryView.getContent();
        }
    }

}