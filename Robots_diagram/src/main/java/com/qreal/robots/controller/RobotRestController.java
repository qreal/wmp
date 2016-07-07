package com.qreal.robots.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qreal.robots.database.robots.service.RobotService;
import com.qreal.robots.model.auth.User;
import com.qreal.robots.model.robot.Robot;
import com.qreal.robots.socket.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by tanvd on 07.07.16.
 */
@RestController
public class RobotRestController {

    @Autowired
    RobotService robotService;

    @ResponseBody
    @RequestMapping(value = "/sendDiagram", method = RequestMethod.POST)
    public String sendProgram(@RequestParam("robotName") String robotName, @RequestParam("program") String program)
            throws JsonProcessingException {
        return robotService.sendProgram(robotName, program);
    }

    @ResponseBody
    @RequestMapping(value = "/registerRobot", method = RequestMethod.POST)
    public String register(@RequestParam("robotName") String name, @RequestParam("ssid") String ssid) {
        return robotService.register(name, ssid);
    }



    @ResponseBody
    @RequestMapping(value = "/deleteRobot", method = RequestMethod.POST)
    public String delete(@RequestParam("robotName") String name) {
        return robotService.delete(name);
    }
}
