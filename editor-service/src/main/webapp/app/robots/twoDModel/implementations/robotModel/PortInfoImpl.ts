import {ReservedVariableType} from "../../types/ReservedVariableType";
import {Direction} from "../../types/Direction";
import {PortInfo} from "../../interfaces/robotModel/PortInfo";
export class PortInfoImpl implements PortInfo {
    private name: string;
    private direction: Direction;
    private nameAliases: string[] = [];
    private reservedVariable: string;
    private reservedVariableType: ReservedVariableType = ReservedVariableType.scalar;

    constructor(name: string, direction: Direction, nameAliases?: string[],
                reservedVariable?: string, reservedVariableType?: ReservedVariableType) {
        this.name = name;
        this.direction = direction;
        this.nameAliases = nameAliases;
        this.reservedVariable = reservedVariable;
        this.reservedVariableType = reservedVariableType;
    }

    getName(): string {
        return this.name;
    }

    getDirection(): Direction {
        return this.direction;
    }

    getNameAliases(): string[] {
        return this.nameAliases;
    }

    getReservedVariable(): string {
        return this.reservedVariable;
    }

    getReservedVariableType(): ReservedVariableType {
        return this.reservedVariableType;
    }
}