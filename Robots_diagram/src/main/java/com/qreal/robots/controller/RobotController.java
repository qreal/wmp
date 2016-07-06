/*
 * Copyright Denis Ageev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.robots.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.robots.model.robot.Message;
import com.qreal.robots.socket.SocketClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by ageevdenis on 02-3-15.
 */
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
