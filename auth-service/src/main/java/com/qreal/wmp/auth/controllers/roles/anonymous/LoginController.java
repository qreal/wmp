package com.qreal.wmp.auth.controllers.roles.anonymous;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Controller of register page.
 * Pages: /log (GET) (login page), /logErr (GET) (login error page)
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Used to retrieve inital request which could be intercepted by SpringSec.
     * It is needed cause standard filter will redirect to initial url, but not oauth filters
     */
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error, ModelMap model)
            throws UnsupportedEncodingException {

        if (error != null) {
            model.addAttribute("error", true);
        } else {
            model.addAttribute("error", false);
        }

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);

        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();

            String redirectUrlEncoded = URLEncoder.encode(redirectUrl, "UTF-8");
            model.addAttribute("redirect", redirectUrlEncoded);
            logger.trace("Someone came to login page with not clean redirect. Added redirect {} to links on page.",
                    redirectUrl);
        }

        return "anonymous/loginView";
    }
}