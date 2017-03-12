package com.qreal.wmp.longpoll.server.thrift;

import com.qreal.wmp.thrift.gen.LongpollThriftService;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;
import org.springframework.context.ApplicationContext;

/**
 * Thrift service class for LongpollRest controller.
 */
public class LongpollServlet extends TServlet {

    public LongpollServlet(ApplicationContext context) {
        super(
                new LongpollThriftService.Processor(new LongpollServletHandler(context)),
                new TJSONProtocol.Factory()
        );
    }
}

