import {Property} from "./Property";
import {Map} from "./Map";
export class PropertiesPack {

    logical: Map<Property>;
    graphical: Map<Property>;

    constructor(logical: Map<Property>, graphical: Map<Property>) {
        this.logical = logical;
        this.graphical = graphical;
    }
}