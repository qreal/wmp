import {Property} from "./Property";
export class SubprogramDiagramNode {

    private logicalId: string;
    private properties: Map<string, Property>;
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

    getProperties(): Map<string, Property> {
        return this.properties;
    }

    private initProperties(name: string): void {
        this.properties = new Map<string, Property>();
        this.properties.set("name", new Property("name", "QString", name));
        this.properties.set("from", new Property("from", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID"));
        this.properties.set("linkShape", new Property("linkShape", "int", "0"));
        this.properties.set("outgoingExplosion", new Property("outgoingExplosion", "qReal::Id", "qrm:/"));
        this.properties.set("to", new Property("to", "qReal::Id", "qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID"));
    }
}