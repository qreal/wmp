export class MathUtils {

    public static toDeg(radians: number): number {
        return radians * (180 / Math.PI);
    }

    public static toRad(degrees: number): number {
        return degrees * (Math.PI / 180);
    }

    public static twoPointLenght(x1: number, y1: number, x2: number, y2: number): number {
        return Math.sqrt(this.sqr(x1 - x2) + this.sqr(y1 - y2));
    }

    public static sqr(x: number): number {
        return x * x;
    }

    public static min(a: number, b: number): number {
        return (a < b) ? a : b;
    }

    public static toRadians(angle : number): number {
        return angle * Math.PI / 180.0;
    }

    public static toDegrees(angle : number): number {
        return angle * 180.0 / Math.PI;
    }

    public static rotateVector(x: number, y: number, angle: number): {x: number, y: number} {
        angle = MathUtils.toRadians(angle);
        var newX: number = x * Math.cos(angle) - y * Math.sin(angle);
        var newY: number = x * Math.sin(angle) + y * Math.cos(angle);
        return { x: newX, y: newY };
    }
}