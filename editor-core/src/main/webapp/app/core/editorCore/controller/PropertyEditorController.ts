import {Property} from "../model/Property";
import {DiagramElement} from "../model/DiagramElement";
import {VariantListMapper} from "./VariantListMapper";
import {ChangePropertyCommand} from "../model/commands/ChangePropertyCommand";
import {Command} from "../model/commands/Command";
import {HtmlView} from "../view/HtmlView";
import {PropertyViewFactory} from "./PropertyViewFactory";
import {UndoRedoController} from "./UndoRedoController";
import {SceneController} from "./SceneController";
import {DiagramNode} from "../model/DiagramNode";
export class PropertyEditorController {

    private propertyViewFactory: PropertyViewFactory;
    private sceneController: SceneController;
    private undoRedoController: UndoRedoController;

    constructor(sceneController: SceneController, undoRedoController: UndoRedoController) {
        this.propertyViewFactory = new PropertyViewFactory();
        this.sceneController = sceneController;
        this.undoRedoController = undoRedoController;
        this.initInputStringListener();
        this.initInputTextListener();
        this.initCheckboxListener();
        this.initDropdownListener();
        this.initSpinnerListener();

        var controller: PropertyEditorController = this;

        document.addEventListener('property-changed', function(e: any) {
            var currentElement: DiagramElement = controller.sceneController.getCurrentElement();
            if (e.detail.key === "InnerText") {
                currentElement.getJointObject().attr("text", {
                    text: e.detail.value
                });
            } else if (e.detail.key === "ChangeType") {
                controller.sceneController.changeElementType(currentElement, e.detail.value);
            } else $("." + e.detail.key + "-" + e.detail.nodeId).each(function(index) {
                if ($(this).val() !== e.detail.value) {
                    $(this).val(e.detail.value);
                    $(this).trigger('autosize');
                }
            })
        }, false);
    }

    public setNodeProperties(element: DiagramElement): void {
        $('#property_table tbody').empty();
        var properties: Map<String, Property> = element.getChangeableProperties();
        for (var property in properties) {
            var propertyView: HtmlView = this.propertyViewFactory.createView(element.getLogicalId(), element.getType(),
                property, properties[property]);
            var htmlElement = $(propertyView.getContent());
            $('#property_table tbody').append(htmlElement);

            if (properties[property].type === "combobox") {
                this.initCombobox(element.getType(), property, htmlElement);
            } else if (properties[property].type === "dropdown") {

            }
        }
    }

    public addChangePropertyCommand(key: string, value: string, changeHtmlFunction: () => void): void {
        var currentElement: DiagramElement = this.sceneController.getCurrentElement();
        var property: Property = currentElement.getChangeableProperties()[key];
        var changePropertyCommand: Command = new ChangePropertyCommand(key, value, property.value,
            this.setProperty.bind(this), changeHtmlFunction);
        this.undoRedoController.addCommand(changePropertyCommand);
    }

    public clearState(): void {
        $(".property").remove();
    }

    public setProperty(key: string, value: string): void {
        var currentElement: DiagramElement = this.sceneController.getCurrentElement();
        var property: Property = currentElement.getChangeableProperties()[key];
        property.value = value;
        currentElement.setProperty(key, property);
    }

    public changeHtmlElementValue(id: string, value: string): void {
        $("#" + id).val(value);
    }

    public changeCheckboxHtml(id: string, value: string): void {
        var tr = $("#" + id).closest('tr');
        var label = $("#" + id).find('label');
        var checkBoxInput = $("#" + id).find('input');
        if (value === "true") {
            label.contents().last()[0].textContent = $("#" + id).data("true");
            checkBoxInput.prop('checked', true);
        } else {
            label.contents().last()[0].textContent =  $("#" + id).data("false");
            checkBoxInput.prop('checked', false);
        }
    }

    private initCombobox(typeName: string, propertyKey: string, element) {
        var variantsList = VariantListMapper.getVariantList(typeName, propertyKey);
        var controller: PropertyEditorController = this;

        element.find('input').autocomplete({
            source: variantsList,
            minLength: 0,
            select: function (event, ui) {
                var key = $(this).data('type');
                var value = ui.item.value;
                controller.addChangePropertyCommand(key, value, () => {});
                controller.setProperty(key, value);
            }
        }).focus(function() {
            $(this).autocomplete("search", $(this).val());
        });
    }

    private initInputStringListener(): void {
        var controller: PropertyEditorController = this;
        $(document).on('input', '.property-edit-input', function () {
            var key = $(this).data('type');
            var value = $(this).val();
            controller.addChangePropertyCommand(key, value, () => {});
            controller.setProperty(key, value);
        });
    }

    private initCheckboxListener(): void {
        var controller: PropertyEditorController = this;
        $(document).on('change', '.checkbox', function () {
            var currentElement: DiagramElement = controller.sceneController.getCurrentElement();
            var key = $(this).data('type');
            var property: Property = currentElement.getChangeableProperties()[key];
            var currentValue = property.value;
            var newValue = controller.changeCheckboxValue(currentValue);
            controller.addChangePropertyCommand(key, newValue, controller.changeCheckboxHtml.bind(
                controller, $(this).attr("id")));
            controller.changeCheckboxHtml($(this).attr("id"), newValue);
            controller.setProperty(key, newValue);
        });
    }

    private initInputTextListener(): void {
        var controller: PropertyEditorController = this;
        $(document).on('input', '.property-edit-text', function () {
            var key = $(this).data('type');
            var value = $(this).val();
            controller.addChangePropertyCommand(key, value, () => {});
            controller.setProperty(key, value);
        });
    }

    private initDropdownListener(): void {
        var controller: PropertyEditorController = this;
        $(document).on('change', '.mydropdown', function () {
            var key = $(this).data('type');
            var value = $(this).val();
            controller.addChangePropertyCommand(key, value, controller.changeHtmlElementValue.bind(
                controller, $(this).attr("id")));
            controller.setProperty(key, value);
        });
    }

    private initSpinnerListener(): void {
        var controller: PropertyEditorController = this;
        $(document).on('change', '.spinner', function () {
            var key = $(this).data('type');
            var value = $(this).val();
            if (value !== "" && !isNaN(value)) {
                controller.addChangePropertyCommand(key, value, controller.changeHtmlElementValue.bind(
                    controller, $(this).attr("id")));
                controller.setProperty(key, value);
            }
        });
    }

    private changeCheckboxValue(value: string): string {
        return (value === "true") ? "false" : "true";
    }

}