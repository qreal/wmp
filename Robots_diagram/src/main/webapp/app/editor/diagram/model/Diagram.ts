class Diagram {

    private id: number;
    private name: string;

    public static createFromJson(diagramJson: any) {
        return new Diagram(diagramJson.diagramId, diagramJson.name)
    }

    constructor(id: number, name: string) {
        this.id = id;
        this.name = name;
    }

    public setId(id: number): void {
        this.id = id;
    }

    public getId(): number {
        return this.id;
    }

    public setName(name: string): void {
        this.name = name;
    }

    public getName(): string {
        return this.name;
    }

}