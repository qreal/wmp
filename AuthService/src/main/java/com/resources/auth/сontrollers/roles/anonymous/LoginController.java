package com.resources.auth.сontrollers.roles.anonymous;

import  javax.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.resources.auth.database.users.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Resource(name="userService")
    private UserDAO userService;

    //Used to retrieve inital request which could be intercepted by SpringSec
    //It is needed cause standard filter will redirect to initial url, but not oauth filters
    private @Autowired
    HttpServletRequest request;

    private @Autowired
    HttpServletResponse response;

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public String login(ModelMap model) throws UnsupportedEncodingException {

        SavedRequest savedRequest =  new HttpSessionRequestCache().getRequest(request, response);

        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();

            String redirectUrlEncoded = URLEncoder.encode(redirectUrl, "UTF-8");
            model.addAttribute("redirect", redirectUrlEncoded);
            logger.trace("Someone came to login page with not clean redirect. Added redirect {} to links on page.", redirectUrl);

        }

        model.addAttribute("error", false);
        return "ROLE_ANONYMOUS/loginView";
    }

    @RequestMapping(value = "/logErr", method = RequestMethod.GET)
    public String loginError(ModelMap model) {
        model.addAttribute("error", true);
        return "ROLE_ANONYMOUS/loginView";
    }
}