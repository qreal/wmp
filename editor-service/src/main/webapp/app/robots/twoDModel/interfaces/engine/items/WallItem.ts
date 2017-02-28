import {AbstractItem} from "./AbstractItem";
export interface WallItem extends AbstractItem {
    getPath(): RaphaelPath;
    updateStart(x: number, y: number): void;
    updateEnd(x: number, y: number): void;
}