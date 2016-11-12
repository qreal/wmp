class StageScroller {

    private stage;
    private zoom: number;

    constructor(zoom: number) {
        this.stage = $("#two-d-model-scene-area");
        this.zoom = zoom;
    }

    public scrollToPoint(x: number, y: number) {
        var width: number = this.stage.width();
        var height: number = this.stage.height();

        var offsetX = x * this.zoom - width / 2;
        var offsetY = y * this.zoom - height / 2;

        this.stage.scrollLeft(offsetX);
        this.stage.scrollTop(offsetY);
    }

}