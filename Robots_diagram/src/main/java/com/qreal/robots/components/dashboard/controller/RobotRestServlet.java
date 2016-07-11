package com.qreal.robots.components.dashboard.controller;

import com.qreal.robots.components.dashboard.thrift.gen.RobotServiceThrift;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;
import org.springframework.context.support.AbstractApplicationContext;

//Rest Controller with Thrift
public class RobotRestServlet extends TServlet {

    //Thrift part of application is not a part of Spring context. But it needs DB services which atre maintained by Spring.
    // So we pass context to Thrift part and it can get DB services from spring part.

    public RobotRestServlet(AbstractApplicationContext context) {
        super(
                new RobotServiceThrift.Processor(new RobotRestServletHandler(context)),
                new TJSONProtocol.Factory()

        );
    }
}



