package com.qreal.wmp.auth.database.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provides access to database for spring security oauth2.
 * It uses clientService to get records from base.
 */
@Service("clientServiceSec")
@Transactional
public class ClientDAOSec implements ClientDetailsService {

    @Autowired
    private ClientDAO clientService;

    public void setClientService(ClientDAO clientService) {
        this.clientService = clientService;
    }

    /** Loads client from local DB by id.*/
    @Override
    public ClientDetails loadClientByClientId(String id) throws ClientRegistrationException {
        ClientDetails clientDetails = clientService.loadClientById(id);
        if (clientDetails == null) {
            throw new ClientRegistrationException("This client not registered");
        }
        return clientDetails;
    }
}
