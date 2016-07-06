/*
 * Copyright Vladimir Zakharov
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.qreal.robots.dao.UserDAO;
import com.qreal.robots.model.auth.User;
import com.qreal.robots.model.robot.Message;
import com.qreal.robots.model.robot.Robot;
import com.qreal.robots.model.robot.RobotInfo;
import com.qreal.robots.model.robot.RobotWrapper;
import com.qreal.robots.service.UserService;
import com.qreal.robots.socket.SocketClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by dageev on 07.03.15.
 */

@Controller
public class MainController {

    public static final String HOST_NAME = "127.0.0.1";
    public static final int PORT = 9002;

    private static final Logger LOG = Logger.getLogger(MainController.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView home(HttpSession session) {
        User user = userService.findByUserName(getUserName());

        List<RobotWrapper> fullRobotInfo = getFullRobotInfo(user.getRobots(), getOnlineRobots(user));
        session.setAttribute("fullRobotInfo", fullRobotInfo);

        ModelAndView model = new ModelAndView();
        model.addObject("user", user);
        model.addObject("robotsWrapper", fullRobotInfo);
        model.setViewName("index");
        return model;
    }

    private List<RobotWrapper> getFullRobotInfo(Set<Robot> robots, List<RobotInfo> onlineUserRobots) {
        List<RobotWrapper> robotsWrapper = Lists.newArrayList();
        for (Robot robot : robots) {
            boolean found = false;
            for (RobotInfo robotInfo : onlineUserRobots) {
                if (robot.getSsid().equals(robotInfo.getSsid())) {
                    found = true;
                    robotsWrapper.add(new RobotWrapper(robot, robotInfo, "Online"));
                }
            }
            if (!found) {
                robotsWrapper.add(new RobotWrapper(robot, "Offline"));
            }
        }
        return robotsWrapper;
    }

    private List<RobotInfo> getOnlineRobots(User user) {
        SocketClient socketClient = new SocketClient(HOST_NAME, PORT);

        if (socketClient.hostAvailable()) {
            try {
                String response = socketClient.sendMessage(getUserOnlineRobots(user));
                return mapper.readValue(response,
                        new TypeReference<List<RobotInfo>>() {
                        });
            } catch (IOException e) {
                LOG.error("Error getting online robots", e);
            }
        } else {
            LOG.warn("Robot routing server is offline. Robot data is unavailable ");
        }
        return Collections.emptyList();

    }

    private String getUserOnlineRobots(User user) throws JsonProcessingException {
        List<RobotInfo> robots = Lists.newArrayList();
        for (Robot robot : user.getRobots()) {
            robots.add(new RobotInfo(user.getUsername(), robot.getName(), robot.getSsid()));
        }

        Message message = new Message("WebApp", "getOnlineRobots", robots);
        return mapper.writeValueAsString(message);
    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
