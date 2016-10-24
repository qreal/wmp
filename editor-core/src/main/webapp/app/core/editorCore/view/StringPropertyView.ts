/// <reference path="HtmlView.ts" />
/// <reference path="../../../core/editorCore/model/Property.ts" />
/// <reference path="../../../utils/StringUtils.ts" />

class StringPropertyView extends HtmlView {

    private template: string = '' +
        '<tr class="property">' +
        '   <td class="vert-align">{0}</td>' +
        '   <td class="vert-align">' +
        '       <div class="input-group">' +
        '           <input class="{1} property-edit-input form-control" type="text" data-type="{2}" value="{3}">' +
        '       </div>' +
        '   </td>' +
        '</tr>';

    constructor(nodeId: string, propertyKey: string, property: Property) {
        super();
        this.content = StringUtils.format(this.template, property.name, propertyKey + "-" + nodeId,
            propertyKey, property.value);
    }

}