import {Property} from "./Property";
import {PropertiesPack} from "./PropertiesPack";
export abstract class DiagramElement {
    abstract getLogicalId(): string;
    abstract getJointObject();
    abstract getName(): string;
    abstract getType(): string;
    abstract getConstPropertiesPack(): PropertiesPack;
    abstract getChangeableProperties(): Map<String, Property>;
    abstract setProperty(name: string, property: Property): void;

    equals(element : DiagramElement) : boolean {
        if (element == undefined) {
            return false;
        }

        let logicalIds = _.isEqual(this.getLogicalId(), element.getLogicalId());
        let jointObjectsByIds = _.isEqual(this.getJointObject().id, element.getJointObject().id);
        let names = _.isEqual(this.getName(), element.getName());
        let types = _.isEqual(this.getType(), element.getType());

        let properties;
        if (this.getConstPropertiesPack() == undefined) {
            properties = element.getConstPropertiesPack() == undefined;
        } else {
            properties = this.getConstPropertiesPack().equals(element.getConstPropertiesPack());
        }

        return logicalIds && jointObjectsByIds && names && types && properties;
    }
}