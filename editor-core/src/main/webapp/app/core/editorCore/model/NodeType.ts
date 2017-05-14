import {Property} from "./Property";
import {StringUtils} from "../../../utils/StringUtils";

export class NodeType {

    private name: string;
    private shownName: string;
    private propertiesMap: Map<String, Property>;
    private image: string;
    private isVisible: Boolean;
    private isImageHidden: Boolean;
    private border: any;
    private innerText: any;

    constructor(name: string, propertiesMap: Map<String, Property>, image: any, border: any, innerText: any, type?: string) {
        this.name = type ? type : name;
        this.shownName = name;
        this.border = border;
        this.innerText = innerText;
        this.propertiesMap = propertiesMap;
        this.image = (image.src) ? StringUtils.format(image.src, this.name) : null;
        this.isImageHidden = (image.isHidden) ? image.isHidden : false;
        this.isVisible = true;
    }

    public getName(): string {
        return this.name;
    }

    public getShownName(): string {
        return this.shownName;
    }

    public getPropertiesMap(): Map<String, Property> {
        return this.propertiesMap;
    }

    public getImage(): string {
        return this.isImageHidden ? "" : this.image;
    }

    public getIcon(): string {
        return this.image;
    }

    public getVisibility(): Boolean {
        return this.isVisible;
    }

    public getBorder(): any {
        return this.border;
    }

    public getInnerText(): any {
        return this.innerText;
    }

    public setVisibility(isVisible: Boolean) {
        this.isVisible = isVisible;
    }

}