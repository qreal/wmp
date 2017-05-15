import {NodeType} from "./NodeType";
import {Property} from "./Property";
export class ContainerNodeType extends NodeType {

    constructor(name: string, propertiesMap: Map<string, Property>, image: string, border: any, innerText: any, path?: string) {
        super(name, propertiesMap, image, border, innerText, path);
    }
}
