package com.qreal.robots.components.authorization.controller;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.database.diagrams.service.client.DiagramService;
import com.qreal.robots.components.database.users.service.client.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Main controller of authentication service.
 * Pages: login, register
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DiagramService diagramService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
        ModelAndView model = new ModelAndView();

        if (error != null) {
            model.addObject("error", "Invalid login or password");
        }

        model.setViewName("auth/login");

        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/register");
        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password,
                                 @RequestParam(value = "password2") String password2,
                                 RedirectAttributes redirectAttributes) {
        ModelAndView model = new ModelAndView();

        if (userService.isUserExist(username)) {
            return registerError(model, "User with " + username + " name is already exist");
        }

        if (!password.equals(password2)) {
            return registerError(model, "Passwords are not equals");
        }
        User user = new User(username, passwordEncoder.encode(password), true);

        logger.info("New user with name {} now will be registered.", username);
        userService.save(user);
        diagramService.createRootFolder(username);
        logger.info("New user with name {} was registered.", username);

        redirectAttributes.addFlashAttribute("msg", "Registered successfully. Log in to continue working");
        model.setViewName("redirect:/login");
        return model;
    }

    private ModelAndView registerError(ModelAndView modelAndView, String message) {
        logger.info("Someone met problem on registration: {}", message);
        modelAndView.addObject("error", message);
        modelAndView.setViewName("auth/register");
        return modelAndView;
    }
}
