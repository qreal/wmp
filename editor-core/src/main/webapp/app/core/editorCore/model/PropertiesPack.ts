import {Property} from "./Property";
export class PropertiesPack {

    logical: Map<string, Property>;
    graphical: Map<string, Property>;

    constructor(logical: Map<string, Property>, graphical: Map<string, Property>) {
        this.logical = logical;
        this.graphical = graphical;
    }
}