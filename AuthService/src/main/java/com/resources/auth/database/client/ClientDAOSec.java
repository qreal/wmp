package com.resources.auth.database.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("clientServiceSec")
@Transactional
public class ClientDAOSec implements ClientDetailsService {

    public void setClientService(ClientDAO clientService) {
        this.clientService = clientService;
    }

    @Autowired
    ClientDAO clientService;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        ClientDetails clientDetails = clientService.loadClientById(s);
        if (clientDetails == null) {
            throw new ClientRegistrationException("This client not registered");
        }
        return clientDetails;
    }
}
