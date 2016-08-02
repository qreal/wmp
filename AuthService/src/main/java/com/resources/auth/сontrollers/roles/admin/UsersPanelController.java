package com.resources.auth.сontrollers.roles.admin;

import com.resources.auth.database.users.User;
import com.resources.auth.database.users.UserAuthority;
import com.resources.auth.database.users.UserDAO;
import com.resources.auth.security.utils.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("usersPanel")
public class UsersPanelController {

    private static final Logger logger = LoggerFactory.getLogger(UsersPanelController.class);

    @Resource(name="userService")
    private UserDAO userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView tableUsersPrepare(ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {
        ModelAndView table = new ModelAndView("ROLE_ADMIN/usersPanel");
        List<User> users = userService.getAll();
        table.addObject("users", users);
        List<String> usersEncoded = new ArrayList<String>();
        for (User user : users) {
            usersEncoded.add(URLEncoder.encode(user.getUsername(), "UTF-8"));
        }
        table.addObject("usersEncoded", usersEncoded);
        table.addObject("name", AuthenticatedUser.getAuthenticatedUserName());
        return table;
    }

    @RequestMapping(value = "grantUserAdminRights/{nameEncoded:.+}", method = RequestMethod.POST)
    public String tableUsersGrantAdmin(@PathVariable("nameEncoded") String nameEncoded) throws UnsupportedEncodingException {
        String name = URLDecoder.decode(nameEncoded, "UTF-8");
        List<User> usersList = userService.get(name);
        User user = usersList.get(0);
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new UserAuthority("ROLE_ADMIN"));
        authorities.add(new UserAuthority("ROLE_USER"));
        user.setAuthorities(authorities);
        userService.edit(user);
        logger.trace("User {} now has admin rights", user.getId());
        return "redirect:/usersPanel";
    }

    @RequestMapping(value = "withdrawUserAdminRights/{nameEncoded:.+}", method = RequestMethod.POST)
    public String tableUsersWithdrawAdmin(@PathVariable("nameEncoded") String nameEncoded) throws UnsupportedEncodingException {
        String name = URLDecoder.decode(nameEncoded, "UTF-8");
        List<User> usersList = userService.get(name);
        User user = usersList.get(0);
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new UserAuthority("ROLE_USER"));
        user.setAuthorities(authorities);
        userService.edit(user);
        logger.trace("Admin {} lost admin rights", user.getId());
        return "redirect:/usersPanel";
    }
}
