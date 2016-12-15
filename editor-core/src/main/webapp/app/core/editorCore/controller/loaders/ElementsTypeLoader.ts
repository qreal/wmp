/// <reference path="../../model/ElementTypes.ts" />
/// <reference path="../parsers/TypesParser.ts" />
/// <reference path="../../../../common/constants/GeneralConstants.d.ts" />
/// <reference path="../../../../vendor.d.ts" />

class ElementsTypeLoader {

    load(callback: (elementTypes: ElementTypes) => void, kit?: string, task?: string): void {
        var typesParser: TypesParser = new TypesParser();

        $.ajax({
            type: 'POST',
            url: 'getTypes/',
            dataType: 'json',
            data: {
                'kit': ((kit) ? kit : GeneralConstants.DEFAULT_KIT),
                'task': task ? task : ""
            },
            success: (response) => {
                callback(typesParser.parse(response));
            },
            error: function (response, status, error) {
                alert("Element types loading error: " + status + " " + error);
            }
        });
    }

}