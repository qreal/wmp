package com.qreal.robots.components.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.dashboard.model.robot.RobotInfo;
import com.qreal.robots.components.dashboard.model.robot.RobotWrapper;
import com.qreal.robots.components.database.users.service.client.UserService;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
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

    @Autowired
    private UserService userService;

    public static final String HOST_NAME = "127.0.0.1";
    public static final int PORT = 9002;

    private static final Logger LOG = Logger.getLogger(MainController.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @RequestMapping("/")
    public ModelAndView home(HttpSession session) {

        User user = null;
        try {
            user = userService.findByUserName(getUserName());
        } catch (TException e) {
            e.printStackTrace();
        }


        Set<Robot> mRobots = user.getRobots();
        List<RobotWrapper> fullRobotInfo = getFullRobotInfo(mRobots, new ArrayList<RobotInfo>());
        session.setAttribute("fullRobotInfo", fullRobotInfo);

        ModelAndView model = new ModelAndView();
        model.addObject("user", user);
        model.addObject("robotsWrapper", fullRobotInfo);
        model.setViewName("dashboard/index");

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
