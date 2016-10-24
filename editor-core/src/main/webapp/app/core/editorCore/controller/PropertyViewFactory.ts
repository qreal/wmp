/// <reference path="../model/Property.ts" />
/// <reference path="../view/HtmlView.ts" />
/// <reference path="../view/StringPropertyView.ts" />
/// <reference path="../view/CheckboxPropertyView.ts" />
/// <reference path="../view/DropdownPropertyView.ts" />
/// <reference path="../view/SpinnerPropertyView.ts" />
/// <reference path="../../../vendor.d.ts" />

class PropertyViewFactory {

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