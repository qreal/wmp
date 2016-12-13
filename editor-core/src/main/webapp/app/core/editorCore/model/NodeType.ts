/// <reference path="Map.ts" />
/// <reference path="Property.ts" />
/// <reference path="../../../vendor.d.ts" />

class NodeType {

    private name: string;
    private id: string;
    private propertiesMap: Map<Property>;
    private image: string;

    constructor(name: string, propertiesMap: Map<Property>, image: string) {
        this.name = name;
        this.propertiesMap = propertiesMap;
        this.image = (image) ? StringUtils.format(image, this.name) : null;
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