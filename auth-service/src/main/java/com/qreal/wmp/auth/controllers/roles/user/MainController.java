package com.qreal.wmp.auth.controllers.roles.user;

import com.qreal.wmp.auth.security.utils.AuthenticatedUser;
import com.qreal.wmp.auth.database.client.Client;
import com.qreal.wmp.auth.database.client.ClientDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainController {
    @Resource(name = "clientService")
    private ClientDAO clientService;

    @RequestMapping(value = "home", method = RequestMethod.GET)
    public ModelAndView tableUsersPrepare(ModelMap model, HttpServletRequest request) {
        ModelAndView table = new ModelAndView("ROLE_USER/mainView");
        List<Client> clientsInBase = clientService.getAll();
        table.addObject("clients", clientsInBase);
        table.addObject("name", AuthenticatedUser.getAuthenticatedUserName());
        return table;
    }
}
