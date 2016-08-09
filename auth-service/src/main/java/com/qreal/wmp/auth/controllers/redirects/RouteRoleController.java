package com.qreal.wmp.auth.controllers.redirects;

import com.qreal.wmp.auth.security.utils.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for routing users with specific roles requesting root to specific paths.
 * Pages: / (GET) (routing processor)
 */
@Controller
public class RouteRoleController {
    private static final Logger logger = LoggerFactory.getLogger(RouteRoleController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String routeRole(ModelMap model) {
        String role = AuthenticatedUser.getAuthenticatedUserAuthorities();
        if (role.contains("ROLE_ADMIN")) {
            logger.trace("Admin {} routed to the main page for role", AuthenticatedUser.getAuthenticatedUserName());
            return "redirect:/clientsPanel";
        }
        else if (role.contains("ROLE_USER")) {
            logger.trace("User {} routed to the main page for role", AuthenticatedUser.getAuthenticatedUserName());
            return "redirect:/home";
        }
        return "redirect:/";
    }
}
