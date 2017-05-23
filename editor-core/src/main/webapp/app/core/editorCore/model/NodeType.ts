import {Property} from "./Property";
import {StringUtils} from "../../../utils/StringUtils";

export class NodeType {

    private name: string;
    private shownName: string;
    private propertiesMap: Map<string, Property>;
    private image: string;
    private isVisible: boolean;
    private isSearchable: boolean;
    private isImageHidden: boolean;
    private static: boolean;
    private border: any;
    private innerText: any;

    constructor(name: string, propertiesMap: Map<string, Property>, image: any, border: any, innerText: any,
                isVisible: boolean, isStatic: boolean, type?: string) {
        this.name = type ? type : name;
        this.shownName = name;
        this.border = border;
        this.innerText = innerText;
        this.propertiesMap = propertiesMap;
        this.image = (image.src) ? StringUtils.format(image.src, this.name) : null;
        this.isImageHidden = (image.isHidden) ? image.isHidden : false;
        this.isVisible = isVisible;
        this.static = isStatic;
        this.isSearchable = true;
    }

    public getName(): string {
        return this.name;
    }

    public getShownName(): string {
        return this.shownName;
    }

    public getPropertiesMap(): Map<string, Property> {
        return this.propertiesMap;
    }

    public getImage(): string {
        return this.isImageHidden ? "" : this.image;
    }

    public getIcon(): string {
        return this.image;
    }

    public getVisibility(): boolean {
        return this.isVisible && this.isSearchable;
    }

    public isStatic(): boolean {
        return this.static;
    }

    public getBorder(): any {
        return this.border;
    }

    public getInnerText(): any {
        return this.innerText;
    }

    public setSearchVisibility(isSearchable: boolean) {
        this.isSearchable = isSearchable;
    }

}