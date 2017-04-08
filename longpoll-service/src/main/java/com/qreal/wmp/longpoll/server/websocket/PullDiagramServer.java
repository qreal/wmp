package com.qreal.wmp.longpoll.server.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;

@Controller
public class PullDiagramServer {

    private PushDiagramServer pushDiagramServer;

    @Autowired
    public PullDiagramServer(PushDiagramServer pushDiagramServer) {
        this.pushDiagramServer = pushDiagramServer;
    }

    @MessageMapping("/push/editors/{editorId}")
    public void sendUpdate(@DestinationVariable String editorId, TextMessage message) {
        System.out.println("sendUpdate got");
        pushDiagramServer.sendPushNotify(Long.valueOf(message.getPayload()), editorId);
    }
}
