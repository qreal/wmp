import {CategoryView} from "./CategoryView";
import {PaletteTree} from "../model/PaletteTree";
import {HtmlView} from "./HtmlView";
//import {Map} from "../model/Map";
export class BlocksPaletteView extends HtmlView {

    constructor(paletteTypes: PaletteTree, elementClass: string) {
        super();
        var categories: Map<String, PaletteTree> = paletteTypes.categories;
        for (var categoryName in categories) {
            var category: PaletteTree = categories[categoryName];
            var categoryView = new CategoryView(categoryName, category, elementClass);
            this.content += categoryView.getContent();
        }
    }

}