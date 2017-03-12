import {Property} from "./Property";
export class PropertiesPack {

    equals(pack : PropertiesPack) : boolean  {
       if (pack == undefined) {
           return false;
       }

       let logical = this.equalsMap(this.logical, pack.logical);
       let graphical = this.equalsMap(this.graphical, pack.graphical);
       return logical && graphical;
    }

    private equalsMap(firstMap : Map<String, Property>, secondMap : Map<String, Property>) {
        for (let key in firstMap.keys()) {
            if (secondMap[key] == undefined || !(secondMap[key].equals(firstMap[key]))) {
                return false;
            }
        }
        for (let key in secondMap.keys()) {
            if (firstMap[key] == undefined || !(firstMap[key].equals(secondMap[key]))) {
                return false;
            }
        }
        return true;
    }

    logical: Map<String, Property>;
    graphical: Map<String, Property>;

    constructor(logical: Map<String, Property>, graphical: Map<String, Property>) {
        this.logical = logical;
        this.graphical = graphical;
    }

}