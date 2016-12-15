/// <reference path="Map.ts" />
/// <reference path="Property.ts" />
/// <reference path="../../../vendor.d.ts" />

class NodeType {

    private name: string;
    private shownName: string;
    private propertiesMap: Map<Property>;
    private image: string;

    constructor(name: string, propertiesMap: Map<Property>, image: string, shownName?: string) {
        this.name = name;
        if (shownName)
            this.shownName = shownName;
        else
            this.shownName = name;
        this.propertiesMap = propertiesMap;
        this.image = (image) ? StringUtils.format(image, this.name) : null;
    }

    public getName(): string {
        return this.name;
    }

    public getShownName(): string {
        return this.shownName;
    }

    public getPropertiesMap(): Map<Property> {
        return this.propertiesMap;
    }

    public getImage(): string {
        return this.image;
    }
}