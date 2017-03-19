import {ReservedVariableType} from "../../types/ReservedVariableType";
import {Direction} from "../../types/Direction";
export interface PortInfo {
    getName(): string;
    getDirection(): Direction;
    getNameAliases(): string[];
    getReservedVariable(): string;
    getReservedVariableType(): ReservedVariableType;
}