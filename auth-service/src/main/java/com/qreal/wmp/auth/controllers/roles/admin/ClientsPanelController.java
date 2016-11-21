package com.qreal.wmp.auth.controllers.roles.admin;

import com.qreal.wmp.auth.database.client.Client;
import com.qreal.wmp.auth.database.client.ClientDAO;
import com.qreal.wmp.auth.security.utils.AuthenticatedUser;
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

/**
 * Controller of clientsPanel page.
 * Pages: /clientsPanel (GET) (admin control page for clients),
 * /clientsPanel/addClient (GET) (adding client interface),
 * /clientsPanel/addClient (POST) (adding client processor),
 * /clientsPanel/configureClient/{client's name} (GET) (configuring client's interface)
 * /clientsPanel/configureClient (POST) (configuring client processor)
 */
@Controller
public class ClientsPanelController {

    private static final Logger logger = LoggerFactory.getLogger(ClientsPanelController.class);

    @Resource(name = "clientService")
    private ClientDAO clientService;

    @RequestMapping(value = "/clientsPanel", method = RequestMethod.GET)
    public ModelAndView tableServersPrepare(ModelMap model, HttpServletRequest request)
            throws UnsupportedEncodingException {
        ModelAndView table = new ModelAndView("admin/clientsPanel");
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

    @RequestMapping(value = "/clientsPanel/addClient", method = RequestMethod.GET)
    public ModelAndView addServer(ModelMap model, HttpServletRequest request) throws IOException {
        ModelAndView modelView = new ModelAndView("admin/ClientsPanelActions/addClient");
        modelView.addObject("name", AuthenticatedUser.getAuthenticatedUserName());
        return modelView;
    }

    @RequestMapping(value = "/clientsPanel/addClient", method = RequestMethod.POST)
    public String serverCheck(ModelMap model, HttpServletRequest request) throws IOException {
        String clientId = request.getParameter("clientId");
        String scopes = request.getParameter("scopes");
        Set<String> scopesSet =  new HashSet<>(Arrays.asList(scopes.split(" ")));
        String secret = request.getParameter("secret");

        //String autoApproveString = request.g("autoApprove");
        //boolean autoApprove = request.getParameterValues("autoApprove");

        Set<String> grantTypes = new HashSet<>();
        grantTypes.add("authorization_code");
        Client client = new Client(clientId, true, secret, true, scopesSet, grantTypes, 64000, 64000, false);
        clientService.add(client);
        logger.trace("New client successfully added with client id {}", client.getClientId());
        return "redirect:/clientsPanel";
    }

    @RequestMapping(value = "/clientsPanel/configureClient", method = RequestMethod.POST)
    public String serverConfigureSave(ModelMap model, HttpServletRequest request) throws IOException {
        String clientId = request.getParameter("clientId");
        String scopes = request.getParameter("scopes");
        String secret = request.getParameter("secret");

        Client client = clientService.loadClientById(clientId);
        if (client == null) {
            return "redirect:/clientsPanel";
        }
        Set<String> scopesSet =  new HashSet<>(Arrays.asList(scopes.split(" ")));
        client.setScope(scopesSet);
        client.setClientSecret(secret);

        clientService.edit(client);
        logger.trace("Client successfully edited with client id {}", client.getClientId());
        return "redirect:/clientsPanel";
    }

    @RequestMapping(value = "/clientsPanel/configureClient/{clientIdEncoded:.+}", method = RequestMethod.GET)
    //need :.+ because of truncating the extension by default
    public ModelAndView configureServer(@PathVariable("clientIdEncoded") String clientIdEncoded,
                                        ModelMap model, HttpServletRequest request)
            throws UnsupportedEncodingException {
        String clientId = URLDecoder.decode(clientIdEncoded, "UTF-8");
        Client client = clientService.loadClientById(clientId);
        ModelAndView modelView = new ModelAndView("admin/ClientsPanelActions/configureClient");
        if (client == null) {
            return modelView;
        }
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
