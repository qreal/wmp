import {SpinnerPropertyView} from "../view/SpinnerPropertyView";
import {DropdownPropertyView} from "../view/DropdownPropertyView";
import {CheckboxPropertyView} from "../view/CheckboxPropertyView";
import {StringPropertyView} from "../view/StringPropertyView";
import {HtmlView} from "../view/HtmlView";
import {Property} from "../model/Property";
export class PropertyViewFactory {

    public createView(nodeId: string, typeName: string, propertyKey: string, property: Property): HtmlView {
        switch (property.type) {
            case "string":
            case "combobox":
                return new StringPropertyView(nodeId, propertyKey, property);
            case "checkbox":
                return new CheckboxPropertyView(typeName, propertyKey, property);
            case "dropdown":
                return new DropdownPropertyView(typeName, propertyKey, property);
            case "spinner":
                return new SpinnerPropertyView(propertyKey, property);
        }
    }
}