/*
 * Copyright Vladimir Zakharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/// <reference path="PropertyViewFactory.ts" />
/// <reference path="PaperController.ts" />
/// <reference path="VariantListMapper.ts" />
/// <reference path="../model/DiagramElement.ts" />
/// <reference path="../model/Map.ts" />
/// <reference path="../model/Property.ts" />
/// <reference path="../view/HtmlView.ts" />
/// <reference path="../../vendor.d.ts" />

class PropertyEditorController {

    private propertyViewFactory: PropertyViewFactory;
    private paperController: PaperController;
    private undoRedoController: UndoRedoController;

    constructor(paperController: PaperController, undoRedoController: UndoRedoController) {
        this.propertyViewFactory = new PropertyViewFactory();
        this.paperController = paperController;
        this.undoRedoController = undoRedoController;
        this.initInputStringListener();
        this.initCheckboxListener();
        this.initDropdownListener();
        this.initSpinnerListener();

        document.addEventListener('property-changed', function(e: any) {
            $("." + e.detail.key + "-" + e.detail.nodeId).each(function(index) {
                if ($(this).val() !== e.detail.value) {
                    $(this).val(e.detail.value);
                    $(this).trigger('autosize');
                }
            })
        }, false);
    }

    public setNodeProperties(element: DiagramElement): void {
        $('#property_table tbody').empty();
        var properties: Map<Property> = element.getChangeableProperties();
        for (var property in properties) {
            var propertyView: HtmlView = this.propertyViewFactory.createView(element.getLogicalId(), element.getType(),
                property, properties[property]);
            var htmlElement = $(propertyView.getContent());
            $('#property_table tbody').append(htmlElement);

            if (properties[property].type === "combobox") {
                this.initCombobox(element.getType(), property, htmlElement);
            }
        }
    }

    public addChangePropertyCommand(key: string, value: string, changeHtmlFunction: () => void): void {
        var currentElement: DiagramElement = this.paperController.getCurrentElement();
        var property: Property = currentElement.getChangeableProperties()[key];
        var changePropertyCommand: Command = new ChangePropertyCommand(key, value, property.value,
            this.setProperty.bind(this), changeHtmlFunction);
        this.undoRedoController.addCommand(changePropertyCommand);
    }

    public clearState(): void {
        $(".property").remove();
    }

    public setProperty(key: string, value: string): void {
        var currentElement: DiagramElement = this.paperController.getCurrentElement();
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
            var currentElement: DiagramElement = controller.paperController.getCurrentElement();
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