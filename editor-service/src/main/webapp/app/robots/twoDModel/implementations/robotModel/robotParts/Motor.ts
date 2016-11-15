/// <reference path="DeviceImpl" />

class Motor extends DeviceImpl {
    static parentType  = DeviceImpl;
    static name: string = "motor";
    static friendlyName: string = "Motor";

    private power: number;

    constructor(power: number = 0) {
        super();
        this.power = power;
    }

    public getPower(): number {
        return this.power;
    }

    public setPower(power: number): void {
        this.power = power;
    }

}