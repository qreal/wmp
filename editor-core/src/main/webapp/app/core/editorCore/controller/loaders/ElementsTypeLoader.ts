import {GeneralConstants} from "../../../../common/constants/GeneralConstants";
import {TypesParser} from "../parsers/TypesParser";
import {ElementTypes} from "../../model/ElementTypes";
export class ElementsTypeLoader {

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
