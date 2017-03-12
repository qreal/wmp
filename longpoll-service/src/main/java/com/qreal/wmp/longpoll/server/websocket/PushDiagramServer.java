package com.qreal.wmp.longpoll.server.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Service;

@Service("pushDiagramServer")
public class PushDiagramServer {

    private static final Logger logger = LoggerFactory.getLogger(PushDiagramServer.class);

    private MessageSendingOperations<String> messagingTemplate;

    @Autowired
    public PushDiagramServer(MessageSendingOperations<String> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendPushNotify(long diagramId) {
        logger.trace("Send push notify called with id {}", diagramId);
        this.messagingTemplate.convertAndSend("/push/diagrams/" + Long.valueOf(diagramId).toString(), "Update");
    }
}
