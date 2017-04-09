import {NodeType} from "./NodeType";
import {Property} from "./Property";
export class ContainerNodeType extends NodeType {
    constructor(name: string, propertiesMap: Map<String, Property>, image: string, path?: string[]) {
        super(name, propertiesMap, image, path);
    }
}