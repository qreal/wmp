import {StringUtils} from "../../../utils/StringUtils";
import {Property} from "../model/Property";
import {HtmlView} from "./HtmlView";
export class SpinnerPropertyView extends HtmlView {

    private template: string = '' +
        '<tr class="property">' +
        '   <td class="vert-align" data-name="propertyName">{0}</td>' +
        '   <td class="vert-align">' +
        '       <input data-name="propertyValue" id="{1}" type="number" data-type="{2}" class="spinner" value="{3}">' +
        '   </td>' +
        '</tr>';

    constructor(propertyKey: string, property: Property) {
        super();
        this.content = StringUtils.format(this.template, property.name, propertyKey + "Property",
            propertyKey, property.value);
    }

}