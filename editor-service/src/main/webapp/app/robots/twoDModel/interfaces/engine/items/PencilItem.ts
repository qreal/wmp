/// <reference path="ColorFieldItem.ts" />

interface PencilItem extends ColorFieldItem {
    updatePath(x: number, y: number): void
}