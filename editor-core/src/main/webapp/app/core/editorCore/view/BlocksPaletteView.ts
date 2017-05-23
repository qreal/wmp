import {CategoryView} from "./CategoryView";
import {PaletteTree} from "../model/PaletteTree";
import {HtmlView} from "./HtmlView";
export class BlocksPaletteView extends HtmlView {

    constructor(paletteTypes: PaletteTree, elementClass: string) {
        super();
        var categories: Map<string, PaletteTree> = paletteTypes.categories;
        for (var [categoryName, category] of categories) {
            var categoryView = new CategoryView(categoryName, category, elementClass);
            this.content += categoryView.getContent();
        }
    }

}