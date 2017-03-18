import {StringUtils} from "../../../utils/StringUtils";
import {VariantListMapper} from "../controller/VariantListMapper";
import {Variant} from "../model/Variant";
import {Property} from "../model/Property";
import {HtmlView} from "./HtmlView";
export class DropdownPropertyView extends HtmlView {

    private template: string = '' +
        '<tr class="property">' +
        '   <td class="vert-align">{0}</td>' +
        '   <td class="vert-align">' +
        '       <select id="{1}" class="mydropdown" data-type="{2}">' +
        '           {3}' +
        '       </select>' +
        '   </td>' +
        '</tr>';

    constructor(typeName: string, propertyKey: string, property: Property) {
        super();
        var variantsList: Variant[] = VariantListMapper.getVariantList(typeName, propertyKey);
        var options: string = '';
        for (var i = 0; i < variantsList.length; i++) {
            var variant = variantsList[i];
            var selected = "";
            if (variant.getKey() === property.value) {
                selected = 'selected = "selected" ';
            }
            options += '<option ' + selected + 'value="' + variant.getKey() + '">' + variant.getValue() + '</option>';
        }
        this.content = StringUtils.format(this.template, property.name, propertyKey + "Property",
            propertyKey, options);
    }

}