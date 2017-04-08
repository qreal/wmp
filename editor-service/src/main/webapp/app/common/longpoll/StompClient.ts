/// <reference path="../../vendor.d.ts" />

import {DiagramMenuController} from "../menu/controller/DiagramMenuController";
import {ServerProperties} from "../properties/ServerProperties";
export class StompClient {

    private webSocket = null;

    private stompClient : Stomp.Client;

    private connected: boolean = false;

    private menuController: DiagramMenuController = null;

    private diagramId : number;

    private editorId : string;

    constructor(menuController: DiagramMenuController) {
        this.menuController = menuController;
    }

    public start(diagramId : number, editorId : string): void {
        console.log("Starting longpoll");
        this.diagramId = diagramId;
        this.editorId = editorId;

        console.log("Longpolling diagramId " + diagramId.toString());
        this.webSocket = new SockJS("http://localhost:" + ServerProperties.portLongpoll + "/diagrams");

        this.stompClient = Stomp.over(this.webSocket);

        let self = this;

        this.stompClient.connect({}, function(frame) {
            self.connected = true;
            console.log("Connected");
            self.stompClient.subscribe("/push/diagrams/" + diagramId.toString(), function(editorIdUpdated) {
                console.log("Got push");
                if (editorId != editorIdUpdated.body) {
                    self.menuController.reloadDiagram();
                }
            });
        });
    }

    public sendUpdateMessage() {
        this.stompClient.send("/messages/push/editors/" + this.editorId.toString(), {}, '"' + this.diagramId.toString() + '"')
    }

    public stop(): void {
        console.log("Stopping longpoll");
        if (this.connected) {
            this.stompClient.disconnect();
            this.connected = false;
            this.diagramId = 0;
            this.editorId = "";
            console.log("Disconnected");
        }
    }
}