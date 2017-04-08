package com.qreal.wmp.longpoll.server.thrift;

import com.qreal.wmp.longpoll.server.websocket.PushDiagramServer;
import com.qreal.wmp.thrift.gen.LongpollThriftService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/** Thrift server side handler for LongpollServlet.*/
public class LongpollServletHandler implements LongpollThriftService.Iface {

    private static final Logger logger = LoggerFactory.getLogger(LongpollServletHandler.class);

    private ApplicationContext context;

    public LongpollServletHandler(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void sendDiagramPush(long id) throws TException {
        PushDiagramServer pushDiagramServer = (PushDiagramServer) context.getBean("pushDiagramServer");
        pushDiagramServer.sendPushNotify(id, "UPDATE");
        logger.error("Send push for diagram {}", id);

    }
}
