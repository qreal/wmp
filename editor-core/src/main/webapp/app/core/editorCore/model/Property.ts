export class Property {

    equals(property : Property) : boolean {
        if (property == undefined) {
            return false;
        }

        let name = _.isEqual(this.name, property.name);
        let type = _.isEqual(this.type, property.type);
        let value = _.isEqual(this.value, property.value);

        return name && type && value;
    }

    name: string;
    type: string;
    value: string;

    constructor(name: string, type: string, value: string) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
}