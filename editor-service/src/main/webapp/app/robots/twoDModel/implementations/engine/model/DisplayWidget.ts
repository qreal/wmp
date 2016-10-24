/// <reference path="LedWidget.ts" />
/// <reference path="../../../utils/MathUtils.ts" />
/// <reference path="../../../../../common/constants/GeneralConstants.ts" />
/// <reference path="../../../../../vendor.d.ts" />

class DisplayWidget {

    private width: number = 218;
    private height: number = 274;
    private smileImg: HTMLImageElement;
    private sadSmileImg: HTMLImageElement;
    private context: any;
    private ledWidget: LedWidget;
    private isSmiles: boolean;
    private isSadSmiles: boolean;
    private background: string;

    constructor() {
        var canvas: HTMLCanvasElement = <HTMLCanvasElement> document.getElementById("display");
        this.context = canvas.getContext("2d");
        this.smileImg = new Image();
        this.smileImg.src = GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/smile.png";
        this.sadSmileImg = new Image();
        this.sadSmileImg.src = GeneralConstants.APP_ROOT_PATH + "images/2dmodel/trikKit/sadSmile.png";
        this.ledWidget = new LedWidget();
        this.isSmiles = false;
        this.isSadSmiles = false;
        this.background = "#a0a0a4";
    }

    setBackground(color: string): void {
        this.background = color;
        $("#display").css('background', color);
    }

    drawSmile(): void {
        this.isSmiles = true;
        this.context.drawImage(this.smileImg, 0, 0, this.width, this.height);
    }

    drawSadSmile(): void {
        this.isSmiles = true;
        this.context.drawImage(this.sadSmileImg, 0, 0, this.width, this.height);
    }

    drawEllipse(x: number, y: number, a: number, b: number, color: string, thickness: number): void {
        this.context.save();
        this.context.beginPath();
        this.context.translate(x, y);
        this.context.scale(a / b, 1);
        this.context.arc(0, 0, b, 0, 2 * Math.PI);
        this.context.restore();
        this.context.lineWidth = thickness;
        this.context.strokeStyle = color;
        this.context.stroke();
    }

    drawArc(x: number, y: number, a: number, b: number, startAngle: number, sweepLength: number,
            color: string, thickness: number): void {
        var startAngleInRad: number = MathUtils.toRad(startAngle);
        var counterStartAngleInRad: number = 2 * Math.PI - startAngleInRad;
        var counterEndAngleInRad: number = 2 * Math.PI - MathUtils.toRad(startAngle + sweepLength);
        this.context.save();
        this.context.beginPath();
        this.context.translate(x, y);
        this.context.scale(a / b, 1);
        this.context.arc(0, 0, b, counterStartAngleInRad, counterEndAngleInRad, true);
        this.context.restore();
        this.context.lineWidth = thickness;
        this.context.strokeStyle = color;
        this.context.stroke();
    }

    drawRectangle(x: number, y: number, a: number, b: number, color: string, thickness: number): void {
        this.context.beginPath();
        this.context.rect(x, y, a, b);
        this.context.lineWidth = thickness;
        this.context.strokeStyle = color;
        this.context.stroke();
    }

    drawLine(x1: number, y1: number, x2: number, y2: number, color: string, thickness: number): void {
        this.context.beginPath();
        this.context.moveTo(x1, y1);
        this.context.lineTo(x2, y2);
        this.context.lineWidth = thickness;
        this.context.strokeStyle = color;
        this.context.stroke();
    }

    drawPoint(x: number, y: number, color: string, thickness: number): void {
        this.context.fillStyle = color;
        this.context.fillRect(x - thickness / 2, y - thickness / 2, thickness, thickness);
    }

    drawText(x: number, y: number, text: string, color: string): void {
        this.context.fillStyle = color;
        this.context.font = "12px Arial";
        this.context.fillText(text, x, y);
    }

    clearSmile(): void {
        this.isSmiles = false;
        this.redraw();
    }

    clearSadSmile(): void {
        this.isSadSmiles = false;
        this.redraw();
    }

    setLedColor(color: string): void {
        this.ledWidget.setColor(color);
    }

    reset(): void {
        this.setBackground("#a0a0a4");
        this.isSmiles = false;
        this.isSadSmiles = false;
        this.clearScreen();
        this.ledWidget.reset()
    }

    redraw(): void {
        this.clearScreen();
        if (this.isSmiles) {
            this.drawSmile();
        }
        if (this.isSadSmiles) {
            this.drawSadSmile();
        }
    }

    clearScreen(): void {
        this.context.clearRect(0, 0, this.width, this.height);
    }

    show(): void {
        $("#hide-controller-model-button").hide();
        $("#hide-controller-model-area").show();
        $("#controller").show();
        $("#display").show();
        $(".port-name").show();
        this.ledWidget.show();
    }

    hide(): void {
        $("#hide-controller-model-area").hide();
        $("#display").hide();
        $("#controller").hide();
        $(".port-name").hide();
        this.ledWidget.hide();
        $("#hide-controller-model-button").show();
    }
    
    displayToFront(): void {
        $("#display").css("z-index", 100);
    }
    
    displayToBack(): void {
        $("#display").css("z-index", -1);
    }
    
}