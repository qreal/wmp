import {StringUtils} from "../../../utils/StringUtils";
import {Property} from "../model/Property";
import {HtmlView} from "./HtmlView";
export class StringPropertyView extends HtmlView {

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