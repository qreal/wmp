import {Property} from "./Property";
import {Map} from "./Map";
export class SubprogramDiagramNode {

    private logicalId: string;
    private properties: Map<Property>;
    private type: string = "SubprogramDiagram";
    private name: string;

    constructor(logicalId: string, name: string) {
        this.logicalId = logicalId;
        this.name = name;
        this.initProperties(name);
    }

    getLogicalId(): string {
        return this.logicalId;
    }

    getType(): string {
        return this.type;
    }

    getName(): string {
        return this.name;
    }

    getProperties(): Map<Property> {
        return this.properties;
    }

    private initProperties(name: string): void {
        this.properties = {};
        this.properties["name"] = new Property("name", "QString", name);
        this.properties["from"] = new Property("from", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
        this.properties["linkShape"] = new Property("linkShape", "int", "0");
        this.properties["outgoingExplosion"] = new Property("outgoingExplosion", "qReal::Id", "qrm:/");
        this.properties["to"] = new Property("to", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID");
    }
}