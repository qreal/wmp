/// <reference path="HtmlView.ts" />
/// <reference path="../../../core/editorCore/model/Property.ts" />
/// <reference path="../../../utils/StringUtils.ts" />

class SpinnerPropertyView extends HtmlView {

    private template: string = '' +
        '<tr class="property">' +
        '   <td class="vert-align">{0}</td>' +
        '   <td class="vert-align">' +
        '       <input id="{1}" type="number" data-type="{2}" class="spinner" value="{3}">' +
        '   </td>' +
        '</tr>';

    constructor(propertyKey: string, property: Property) {
        super();
        this.content = StringUtils.format(this.template, property.name, propertyKey + "Property",
            propertyKey, property.value);
    }

}