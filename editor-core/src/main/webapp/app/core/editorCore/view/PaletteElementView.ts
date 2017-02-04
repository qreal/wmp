import {StringUtils} from "../../../utils/StringUtils";
import {HtmlView} from "./HtmlView";
export class PaletteElementView extends HtmlView {

    private imageWidth: number = 30;
    private imageHeight: number = 30;

    private template: string = '' +
        '<li>' +
        '   <div class="{0}" data-type="{1}">' +
        '       <img class="element-img" src="{2}" width="{3}" height="{4}">' +
        '       {5}' +
        '   </div>' +
        '</li>';

    constructor(typeName: string, name: string, imageSrc: string, elementClass: string) {
        super();
        this.content = StringUtils.format(this.template, elementClass, typeName, imageSrc,
            this.imageWidth.toString(), this.imageHeight.toString(), name);
    }

}