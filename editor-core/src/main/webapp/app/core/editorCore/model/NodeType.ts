import {Property} from "./Property";
import {StringUtils} from "../../../utils/StringUtils";

export class NodeType {

    private name: string;
    private shownName: string;
    private propertiesMap: Map<String, Property>;
    private image: string;
    private isVisible: Boolean;
    private isImageHidden: Boolean;

    constructor(name: string, propertiesMap: Map<String, Property>, image: any, path?: string[]) {
        if (path) {
            this.name = path.reverse().join('-');
            path.reverse();
        } else
            this.name = name;
        this.shownName = name;
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

    public setVisibility(isVisible: Boolean) {
        this.isVisible = isVisible;
    }

}