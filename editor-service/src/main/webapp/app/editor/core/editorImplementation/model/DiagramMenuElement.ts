class DiagramMenuElement {

    private name: string;
    private type: string;

    constructor(name: string, type: string) {
        this.name = name;
        this.type = type;
    }

    public getName(): string {
        return this.name;
    }

    public getType(): string {
        return this.type;
    }

}