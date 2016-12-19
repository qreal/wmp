/// <reference path="HtmlView.ts" />
/// <reference path="../../../utils/StringUtils.ts" />

class SubprogramPaletteElementView extends HtmlView {

    private imageWidth: number = 30;
    private imageHeight: number = 30;

    private template: string = '' +
        '<li>' +
        '   <div class="tree-element" data-type="{0}" data-name="{1}" data-id="{2}">' +
        '       <img class="element-img" src="{3}" width="{4}" height="{5}">' +
        '       {6}' +
        '   </div>' +
        '</li>';

    constructor(typeName: string, name: string, imageSrc: string, nodeLogicalId: string) {
        super();
        this.content = StringUtils.format(this.template, typeName, name, nodeLogicalId, imageSrc,
            this.imageWidth.toString(), this.imageHeight.toString(), name);
    }

}