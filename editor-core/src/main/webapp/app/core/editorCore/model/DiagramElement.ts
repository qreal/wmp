import {Property} from "./Property";
import {PropertiesPack} from "./PropertiesPack";
export interface DiagramElement {
    getLogicalId(): string;
    getJointObject();
    getName(): string;
    getType(): string;
    getConstPropertiesPack(): PropertiesPack;
    getChangeableProperties(): Map<String, Property>;
    setProperty(name: string, property: Property): void;
}