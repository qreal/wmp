package com.resources.auth.controllers.roles.admin;

import com.resources.auth.database.client.Client;
import com.resources.auth.database.client.ClientDAO;
import com.resources.auth.security.utils.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("clientsPanel")
public class ClientsPanelController {

    private static final Logger logger = LoggerFactory.getLogger(ClientsPanelController.class);

    @Resource(name = "clientService")
    private ClientDAO clientService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView tableServersPrepare(ModelMap model, HttpServletRequest request)
            throws UnsupportedEncodingException {
        ModelAndView table = new ModelAndView("ROLE_ADMIN/clientsPanel");
        List<Client> clientsInBase = clientService.getAll();
        table.addObject("clients", clientsInBase);
        List<String> clientsEncoded = new ArrayList<String>();
        for (Client client : clientsInBase) {
            clientsEncoded.add(URLEncoder.encode(client.getClientId(), "UTF-8"));
        }
        table.addObject("clientsEncoded", clientsEncoded);
        table.addObject("name", AuthenticatedUser.getAuthenticatedUserName());
        return table;
    }

    @RequestMapping(value = "addClient", method = RequestMethod.GET)
    public ModelAndView addServer(ModelMap model, HttpServletRequest request) throws IOException {
        ModelAndView modelView = new ModelAndView("ROLE_ADMIN/ClientsPanelActions/addClient");
        modelView.addObject("name", AuthenticatedUser.getAuthenticatedUserName());
        return modelView;
    }

    @RequestMapping(value = "addClient", method = RequestMethod.POST)
    public String serverCheck(ModelMap model, HttpServletRequest request) throws IOException {
        String clientId = request.getParameter("clientId");
        String scopes = request.getParameter("scopes");
        Set<String> scopesSet =  new HashSet<String>(Arrays.asList(scopes.split(" ")));
        String secret = request.getParameter("secret");

        //String autoApproveString = request.g("autoApprove");
        //boolean autoApprove = request.getParameterValues("autoApprove");

        Set<String> grantTypes = new HashSet<String>();
        grantTypes.add("authorization_code");
        Client client = new Client(clientId, true, secret, true, scopesSet, grantTypes, 64000, 64000, false);
        clientService.add(client);
        logger.trace("New client successfully added with client id {}", client.getClientId());
        return "redirect:/clientsPanel";
    }

    @RequestMapping(value = "configureClient", method = RequestMethod.POST)
    public String serverConfigureSave(ModelMap model, HttpServletRequest request) throws IOException {
        String clientId = request.getParameter("clientId");
        String scopes = request.getParameter("scopes");
        String secret = request.getParameter("secret");
        List<Client> clients = clientService.get(clientId);

        Client client = clients.get(0);

        Set<String> scopesSet =  new HashSet<String>(Arrays.asList(scopes.split(" ")));
        client.setScope(scopesSet);

        client.setClientSecret(secret);
        if (clients.isEmpty()) {
            return "redirect:/clientsPanel";
        }
        clientService.edit(client);
        logger.trace("Client successfully edited with client id {}", client.getClientId());
        return "redirect:/clientsPanel";
    }

    @RequestMapping(value = "configureClient/{clientIdEncoded:.+}", method = RequestMethod.GET)
    //need :.+ because of truncating the extension by default
    public ModelAndView configureServer(@PathVariable("clientIdEncoded") String clientIdEncoded,
                                        ModelMap model, HttpServletRequest request)
            throws UnsupportedEncodingException {
        String clientId = URLDecoder.decode(clientIdEncoded, "UTF-8");
        List<Client> clients = clientService.get(clientId);
        ModelAndView modelView = new ModelAndView("ROLE_ADMIN/ClientsPanelActions/configureClient");
        if (clients.isEmpty()) {
            return modelView;
        }
        Client client = clients.get(0);
        modelView.addObject("name", AuthenticatedUser.getAuthenticatedUserName());
        modelView.addObject("clientId", client.getClientId());

        String scopes = "";
        Set<String> scopeSet = client.getScope();
        for (String scope : scopeSet) {
            scopes += scope + " ";
        }
        modelView.addObject("scopes", scopes);

        modelView.addObject("secret", client.getClientSecret());

        logger.trace("Starting configuring of client with id {}", clientId);
        return modelView;
    }
}
