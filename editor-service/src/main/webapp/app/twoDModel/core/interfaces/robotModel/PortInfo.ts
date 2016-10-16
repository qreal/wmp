/// <reference path="../../types/Direction.ts" />
/// <reference path="../../types/ReservedVariableType.ts" />

interface PortInfo {
    getName(): string;
    getDirection(): Direction;
    getNameAliases(): string[];
    getReservedVariable(): string;
    getReservedVariableType(): ReservedVariableType;
}