import {StringUtils} from "../../../utils/StringUtils";
import {Variant} from "../model/Variant";
import {Property} from "../model/Property";
import {HtmlView} from "./HtmlView";
import {VariantListMapper} from "../controller/VariantListMapper";
export class CheckboxPropertyView extends HtmlView {

    private template: string = '' +
        '<tr class="property">' +
        '   <td class="vert-align">{0}</td>' +
        '   <td class="vert-align">' +
        '       <div id="{1}" class="checkbox" data-type="{2}" data-true="{3}" data-false="{4}">' +
        '           <label class="active"><input type="checkbox" {5}>{6}</label>' +
        '       </div>' +
        '   </td>' +
        '</tr>';

    constructor(typeName: string, propertyKey: string, property: Property) {
        super();
        var variantsList: Variant[] = VariantListMapper.getVariantList(typeName, propertyKey);
        var dataTrue: string;
        var dataFalse: string;
        for (var i = 0; i < variantsList.length; i++) {
            if (variantsList[i].getKey() === "true") {
                dataTrue = variantsList[i].getValue();
            }
            if (variantsList[i].getKey() === "false") {
                dataFalse = variantsList[i].getValue();
            }
        }

        var visibleValue: string;
        if (property.value === variantsList[0].getKey()) {
            visibleValue = variantsList[0].getValue();
        } else {
            visibleValue = variantsList[1].getValue();
        }
        var state: string = "";
        if (property.value === "true") {
            state = "checked";
        }

        this.content = StringUtils.format(this.template, property.name, propertyKey + "Property",
            propertyKey, dataTrue, dataFalse, state, visibleValue);
    }

}