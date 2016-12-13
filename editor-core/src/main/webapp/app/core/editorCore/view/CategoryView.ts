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

    constructor(categoryName: string, category: Map<NodeType>) {
        super();
        var elementsContent: string = '';
        for (var typeName in category) {
            var nodeType: NodeType = category[typeName];
            var paletteElementView: PaletteElementView = new PaletteElementView(typeName, nodeType.getName(), nodeType.getImage());
            elementsContent += paletteElementView.getContent();
        }
        this.content = StringUtils.format(this.template, categoryName, elementsContent);
    }

}