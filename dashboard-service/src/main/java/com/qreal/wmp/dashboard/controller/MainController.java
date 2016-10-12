package com.qreal.wmp.dashboard.controller;

import com.qreal.wmp.dashboard.common.utils.AuthenticatedUser;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.dashboard.database.exceptions.NotFoundException;
import com.qreal.wmp.dashboard.database.users.client.UserService;
import com.qreal.wmp.dashboard.database.users.model.User;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Main controller of Dashboard service.
 * Pages: / (main page)
 */
@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public ModelAndView home(HttpSession session) {
        logger.info("User {} requested main page.", AuthenticatedUser.getUserName());

        User user = null;
        try {
            user = userService.findByUserName(AuthenticatedUser.getUserName());
        } catch (NotFoundException notFound) {
            logger.error("Authentication error: authenticated user not in database.");
        } catch (ErrorConnectionException errorConnection) {
            logger.error("Connection error: user service not online.");
        } catch (TException e) {
            //Should never happen
            e.printStackTrace();
        }

        ModelAndView model = new ModelAndView();
        if (user != null) {
            model.addObject("user", user);
            model.addObject("robots", user.getRobots());
        }
        model.setViewName("dashboard/index");

        logger.info("For user {} main page was created", AuthenticatedUser.getUserName());

        return model;
    }

}
