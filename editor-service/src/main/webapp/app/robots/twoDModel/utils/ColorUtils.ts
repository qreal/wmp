import {RGBAColor} from "./RGBAColor";
export class ColorUtils {

    public static getRBGAColor(color: string): RGBAColor {
        if (color.length == 9) {
            var rgb: string = "#" + color.substr(3, color.length - 3);
            var alpha: number = this.alphaHexTo0To1(color.substr(1, 2));
            return new RGBAColor(alpha, rgb);
        } else {
            return new RGBAColor(1, color);
        }
    }

    public static alphaHexTo0To1(alpha: string): number {
        var decAlpha: number = parseInt(alpha, 16);
        return decAlpha / 255;
    }
}