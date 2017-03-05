/// <reference path="Map.ts" />
/// <reference path="Property.ts" />
/// <reference path="../../../vendor.d.ts" />

const defaultNodeWidth = 50;
const defaultNodeHeight = 50;

class NodeType {

    private name: string;
    private shownName: string;
    private propertiesMap: Map<Property>;
    private image: string;
    private isVisible: Boolean;

    constructor(name: string, propertiesMap: Map<Property>, image: string, path?: string[]) {
        if (path) {
            this.name = path.reverse().join('-');
            path.reverse();
        } else
            this.name = name;
        this.shownName = name;
        this.propertiesMap = propertiesMap;
        this.image = (image) ? StringUtils.format(image, this.name) : null;
        this.isVisible = true;
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

    public getVisibility(): Boolean {
        return this.isVisible;
    }

    public setVisibility(isVisible: Boolean) {
        this.isVisible = isVisible;
    }
}
