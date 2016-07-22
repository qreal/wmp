package com.qreal.robots.components.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.dashboard.model.robot.RobotInfo;
import com.qreal.robots.components.dashboard.model.robot.RobotWrapper;
import com.qreal.robots.components.database.users.service.client.UserService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public static final String HOST_NAME = "127.0.0.1";

    public static final int PORT = 9002;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView home(HttpSession session) {
        logger.info("User {} requested main page.", getUserName());

        User user = userService.findByUserName(getUserName());

        Set<Robot> mRobots = user.getRobots();
        List<RobotWrapper> fullRobotInfo = getFullRobotInfo(mRobots, new ArrayList<RobotInfo>());
        session.setAttribute("fullRobotInfo", fullRobotInfo);

        ModelAndView model = new ModelAndView();
        model.addObject("user", user);
        model.addObject("robotsWrapper", fullRobotInfo);
        model.setViewName("dashboard/index");

        logger.info("For user {} main page was created", getUserName());

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

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
