package com.qreal.wmp.dashboard.controller;

import com.qreal.wmp.thrift.gen.RobotServiceThrift;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;
import org.springframework.context.ApplicationContext;

/** Thrift service class for RobotRest controller.*/
public class RobotRestServlet extends TServlet {

    //Thrift part of application is not a part of Spring context. But it needs DB services which
    // are maintained by Spring.
    // So we pass context to Thrift part and it can get DB services from spring part.

    /** Constructor with context param. It weaves context of TServlet in context of spring application.*/
    public RobotRestServlet(ApplicationContext applicationContext) {
        super(
                new RobotServiceThrift.Processor(new RobotRestServletHandler(applicationContext)),
                new TJSONProtocol.Factory()
        );
    }
}
