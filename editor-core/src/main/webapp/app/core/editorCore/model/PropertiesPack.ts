/// <reference path="Map.ts" />
/// <reference path="Property.ts" />

class PropertiesPack {

    logical: Map<Property>;
    graphical: Map<Property>;

    constructor(logical: Map<Property>, graphical: Map<Property>) {
        this.logical = logical;
        this.graphical = graphical;
    }
}