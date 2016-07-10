package com.qreal.robots.components.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qreal.robots.components.database.robots.service.RobotService;
import org.apache.tiles.request.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import com.qreal.robots.components.dashboard.thrift.gen.RobotServiceThrift;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

//Rest Controller with Spring
//@RestController
//public class RobotRestController {
//
//    @Autowired
//    RobotService robotService;
//
//    @ResponseBody
//    @RequestMapping(value = "/sendDiagram", method = RequestMethod.POST)
//    public String sendProgram(@RequestParam("robotName") String robotName, @RequestParam("program") String program)
//            throws JsonProcessingException {
//        return robotService.sendProgram(robotName, program);
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/registerRobot", method = RequestMethod.POST)
//    public String register(@RequestParam("robotName") String name, @RequestParam("ssid") String ssid) {
//        return robotService.register(name, ssid);
//    }
//
//
//
//    @ResponseBody
//    @RequestMapping(value = "/deleteRobot", method = RequestMethod.POST)
//    public String delete(@RequestParam("robotName") String name) {
//        return robotService.delete(name);
//    }
//}


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



