/// <reference path="ColorFieldItem.ts" />
/// <reference path="../../../../../vendor.d.ts"/>

interface CubicBezierItem extends ColorFieldItem {
    getPath(): RaphaelPath;
    updateStart(x: number, y: number): void;
    updateEnd(x: number, y: number): void;
    updateCP1(x: number, y: number): void;
    updateCP2(x: number, y: number): void;
}