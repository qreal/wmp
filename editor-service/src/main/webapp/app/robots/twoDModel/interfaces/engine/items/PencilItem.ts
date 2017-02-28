import {ColorFieldItem} from "./ColorFieldItem";
export interface PencilItem extends ColorFieldItem {
    updatePath(x: number, y: number): void
}
