/// <reference path="../../vendor.d.ts" />

import {DiagramMenuController} from "../menu/controller/DiagramMenuController";
import {ServerProperties} from "../properties/ServerProperties";
export class StompClient {

    private webSocket = null;

    private stompClient : Stomp.Client;

    private connected: boolean = false;

    private menuController: DiagramMenuController = null;

    constructor(menuController: DiagramMenuController) {
        this.menuController = menuController;
    }

    public start(diagramId : number): void {
        console.log("Starting longpoll");

        console.log("Longpolling diagramId " + diagramId.toString());
        this.webSocket = new SockJS("http://localhost:" + ServerProperties.portLongpoll + "/diagrams");

        this.stompClient = Stomp.over(this.webSocket);

        let self = this;

        this.stompClient.connect({}, function(frame) {
            self.connected = true;
            console.log("Connected");
            self.stompClient.subscribe("/push/diagrams/" + diagramId.toString(), function(messageOutput) {
                console.log("Got push");
                self.menuController.reloadDiagram();
            });
        });
    }

    public stop(): void {
        console.log("Stopping longpoll");
        if (this.connected) {
            this.stompClient.disconnect();
            this.connected = false;
            console.log("Disconnected");
        }
    }
}