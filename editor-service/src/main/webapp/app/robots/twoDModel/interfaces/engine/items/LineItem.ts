import {ColorFieldItem} from "./ColorFieldItem";
export interface LineItem extends ColorFieldItem {
    getPath(): RaphaelPath;
    updateStart(x: number, y: number): void;
    updateEnd(x: number, y: number): void;
}