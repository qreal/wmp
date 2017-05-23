import {StringUtils} from "../../../utils/StringUtils";
import {Property} from "./Property";
export class PropertyEditElement {

    private static propertyTemplate = "" +
        "<span>{3}:</span> " +
        "<input class='{0} property-edit-input' data-id='{1}' data-type='{2}' " +
        "style='border: dashed 1px; padding-left: 2px; margin-bottom: 1px' value='{4}'>" +
        "<br>";

    private static template: string = "" +
        "<div class='property-edit-element' style='position: absolute; text-align: left; z-index: 1;'>" +
        "   {0}" +
        "</div>";

    private htmlElement;

    constructor(logicalId: string, jointObjectId: string, properties: Map<string, Property>) {
        var propertiesHtml: string = "";

        for (var [propertyKey, property] of properties) {
            if (property.type === "string") {
                propertiesHtml += StringUtils.format(PropertyEditElement.propertyTemplate,
                    propertyKey + "-" + logicalId, jointObjectId, propertyKey, property.name, property.value);
                break;
            }
        }

        this.htmlElement = $(StringUtils.format(PropertyEditElement.template, propertiesHtml));
        this.initInputSize();
        this.initInputAutosize();
    }

    public getHtmlElement() {
        return this.htmlElement;
    }

    public setPosition(x: number, y: number): void {
        this.htmlElement.css({ left: x - 25, top: y + 55 });
    }

    private initInputSize(): void {
        this.htmlElement.find('input').each(function(index) {
                $(this).css("width", StringUtils.getInputStringSize(this));
            }
        );

    }

    private initInputAutosize(): void {
        this.htmlElement.find('input').on('input', function(event) {
            $(this).trigger('autosize');
        });

        this.htmlElement.find('input').on('autosize', function(event) {
            $(this).css("width", StringUtils.getInputStringSize(this));
        });
    }

}