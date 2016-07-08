package com.qreal.robots.components.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.robots.components.dashboard.model.robot.Message;
import com.qreal.robots.common.socket.SocketClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RobotController {

    public static final String HOST_NAME = "127.0.0.1";
    public static final int PORT = 9002;
    private static final ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/robot", method = RequestMethod.GET)
    public ModelAndView viewRobot() {
        ModelAndView model = new ModelAndView();
        model.setViewName("robot/robot");
        return model;
    }



    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String map(Model model) throws JsonProcessingException {
        SocketClient socketClient = new SocketClient(HOST_NAME, PORT);
        String robots = socketClient.sendMessage(generateGetOnlineRobotsRequest());

        model.addAttribute("robots", robots);
        return "robot/map";
    }


    private String generateGetOnlineRobotsRequest() throws JsonProcessingException {
        Message message = new Message("WebApp", "getOnlineRobots");
        return mapper.writeValueAsString(message);
    }

}
