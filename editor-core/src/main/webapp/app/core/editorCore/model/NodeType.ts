/// <reference path="Map.ts" />
/// <reference path="Property.ts" />
/// <reference path="../../../vendor.d.ts" />

const defaultNodeWidth = 50;
const defaultNodeHeight = 50;

class NodeType {

    private name: string;
    private propertiesMap: Map<Property>;
    private image: string;

    constructor(name: string, propertiesMap: Map<Property>, image?: string) {
        this.name = name;
        this.propertiesMap = propertiesMap;
        this.image = (image) ? image : null;
    }

    public getName(): string {
        return this.name;
    }

    public getPropertiesMap(): Map<Property> {
        return this.propertiesMap;
    }

    public getImage(): string {
        return this.image;
    }

}