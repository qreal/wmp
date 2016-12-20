/// <reference path="DiagramScene.ts" />
/// <reference path="../../../vendor.d.ts" />

class DiagramEditor {

    private graph: joint.dia.Graph;
    private scene: DiagramScene;

    constructor() {
        this.graph = new joint.dia.Graph;
        this.scene = new DiagramScene("diagram-scene", this.graph);
    }

    public getGraph(): joint.dia.Graph {
        return this.graph;
    }

    public getScene(): DiagramScene {
        return this.scene;
    }

    public clear(): void {
        this.scene.clear();
        this.graph.clear();
    }

}