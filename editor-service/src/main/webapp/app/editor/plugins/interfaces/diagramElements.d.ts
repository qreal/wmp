declare class Property {
    name: string;
    type: string;
    value: string;
    constructor(name: string, type: string, value: string);
}

declare class PropertiesPack {
    logical: Map<Property>;
    graphical: Map<Property>;
    constructor(logical: Map<Property>, graphical: Map<Property>);
}

declare interface DiagramElement {
    getLogicalId(): string;
    getJointObject();
    getName(): string;
    getType(): string;
    getConstPropertiesPack(): PropertiesPack;
    getChangeableProperties(): Map<Property>;
    setProperty(name: string, property: Property): void;
}

declare class Link implements DiagramElement {
    constructor(jointObject: joint.dia.Link, properties: Map<Property>);
    getLogicalId(): string;
    getJointObject(): any;
    getName(): string;
    getType(): string;
    getConstPropertiesPack(): PropertiesPack;
    getChangeableProperties(): Map<Property>;
    setProperty(key: string, property: Property): void;
}

declare interface DiagramNode extends DiagramElement {
    getX(): number;
    getY(): number;
    getImagePath(): string;
    setPosition(x: number, y: number, zoom: number): void;
    getChangeableProperties(): Map<Property>;
    initPropertyEditElements(zoom: number): void;
}
