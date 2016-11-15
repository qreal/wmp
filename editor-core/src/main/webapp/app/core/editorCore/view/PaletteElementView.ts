/// <reference path="HtmlView.ts" />
/// <reference path="../../../utils/StringUtils.ts" />

class PaletteElementView extends HtmlView {

    private imageWidth: number = 30;
    private imageHeight: number = 30;

    private template: string = '' +
        '<li>' +
        '   <div class="tree-element" data-type="{0}">' +
        '       <img class="element-img" src="{1}" width="{2}" height="{3}">' +
        '       {4}' +
        '   </div>' +
        '</li>';

    constructor(typeName: string, name: string, imageSrc: string) {
        super();
        this.content = StringUtils.format(this.template, typeName, imageSrc,
            this.imageWidth.toString(), this.imageHeight.toString(), name);
    }

}