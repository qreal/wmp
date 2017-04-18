import {NodeType} from "./NodeType";
import {Property} from "./Property";
export class ContainerNodeType extends NodeType {

    private border: any;

    constructor(name: string, propertiesMap: Map<String, Property>, image: string, border: any, path?: string[]) {
        super(name, propertiesMap, image, path);
        this.border = border;
    }

    public getBorder(): any {
        return this.border;
    }
}