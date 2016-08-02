package com.qreal.robots.controller;

import com.qreal.robots.thrift.gen.RobotServiceThrift;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Thrift service class for RobotRest controller.
 */
public class RobotRestServlet extends TServlet {

    //Thrift part of application is not a part of Spring context. But it needs DB services which
    // are maintained by Spring.
    // So we pass context to Thrift part and it can get DB services from spring part.

    /**
     * Constructor with context param. It weaves context of TServlet in context of spring application.
     */
    public RobotRestServlet(AbstractApplicationContext context) {
        super(
                new RobotServiceThrift.Processor(new RobotRestServletHandler(context)),
                new TJSONProtocol.Factory()
        );
    }
}
