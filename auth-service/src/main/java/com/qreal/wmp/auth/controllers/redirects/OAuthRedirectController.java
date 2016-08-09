package com.qreal.wmp.auth.controllers.redirects;

import com.qreal.wmp.auth.security.utils.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

/**
 * Controller for redirections after authentication through 3dparty providers.
 * Pages: /oauth/google (GET) (google authentication path), /oauth/github (GET) (github authentication point)
 */
@Controller
public class OAuthRedirectController {

    private static final Logger logger = LoggerFactory.getLogger(OAuthRedirectController.class);

    /**
     * Redirection for google 3dparty provider. After authentication user will be redirect either to his main page
     * or to his redirect parameter.
     */
    @RequestMapping(value = {"/oauth/google"},  method = RequestMethod.GET)
    public String routeGoogle(@RequestParam("redirect") Optional<String> redirect, ModelMap model)
            throws UnsupportedEncodingException {
        if (redirect.isPresent() && !redirect.get().equals(""))
        {
            String redirectDecoded = URLDecoder.decode(redirect.get(), "UTF-8");
            logger.trace("User {} logged in via google and redirected to {}",
                    AuthenticatedUser.getAuthenticatedUserName(), redirectDecoded);
            return "redirect:" + redirectDecoded;
        }
        String role = AuthenticatedUser.getAuthenticatedUserAuthorities();
        if (role.contains("ROLE_ADMIN")) {
            logger.trace("Admin {} logged in via google and redirected to usersPanel",
                    AuthenticatedUser.getAuthenticatedUserName());
            return "redirect:/clientsPanel";
        }
        else if (role.contains("ROLE_USER")) {
            logger.trace("User {} logged in via google and redirected to userServers",
                    AuthenticatedUser.getAuthenticatedUserName());
            return "redirect:/home";
        }
        return "redirect:/";
    }

    /**
     * Redirection for github 3dparty provider. After authentication user will be redirect either to his main page
     * or to his redirect parameter.
     */
    @RequestMapping(value = {"/oauth/github"},  method = RequestMethod.GET)
    public String routeGithub(@RequestParam("redirect") Optional<String> redirect, ModelMap model)
            throws UnsupportedEncodingException {
        if (redirect.isPresent() && !redirect.get().equals(""))
        {
            String redirectDecoded = URLDecoder.decode(redirect.get(), "UTF-8");
            logger.trace("User {} logged in via github and redirected to {}",
                    AuthenticatedUser.getAuthenticatedUserName(), redirectDecoded);
            return "redirect:" + redirectDecoded;
        }
        String role = AuthenticatedUser.getAuthenticatedUserAuthorities();
        if (role.contains("ROLE_ADMIN")) {
            logger.trace("Admin {} logged in via github and redirected to usersPanel",
                    AuthenticatedUser.getAuthenticatedUserName());
            return "redirect:/clientsPanel";
        }
        else if (role.contains("ROLE_USER")) {
            logger.trace("User {} logged in via github and redirected to userServers",
                    AuthenticatedUser.getAuthenticatedUserName());
            return "redirect:/home";
        }
        return "redirect:/";
    }
}
