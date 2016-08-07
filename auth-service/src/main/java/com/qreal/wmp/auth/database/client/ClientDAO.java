package com.qreal.wmp.auth.database.client;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/** This is the main class for work with client table in database.*/
@Service("clientService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ClientDAO {

    private static final Logger logger = LoggerFactory.getLogger(ClientDAO.class);

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Retrieves a single client by idClient.
     * (Take first from list of returned)
     */
    public Client loadClientById(String clientId) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("FROM Client E WHERE E.clientId = :clientId");
        query.setParameter("clientId", clientId);
        List<Client> results = query.list();
        if (results.size() != 1) {
            logger.trace("Client {} was not found using client id", clientId);
            return null;
        }
        logger.trace("Client taken from database using client id {}", clientId);
        return results.get(0);
    }

    /** Retrieves all clients from database.*/
    public List<Client> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Client E");
        List<Client> results = query.list();
        logger.trace("{} clients were loaded from database", results.size());
        return results;
    }

    /** Adds a new client.*/
    public void add(Client client) {
        if (loadClientById(client.getClientId()) != null) {
            return;
        }
        Session session = sessionFactory.getCurrentSession();
        session.save(client);
        logger.trace("{} client was saved to database", client.getClientId());

    }

    /** Deletes an existing client by id.*/
    public void delete(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, id);
        session.delete(client);
        logger.trace("{} client with id {} was deleted from database", client.getClientId(), id);
    }

    /** Edits an existing client.*/
    public void edit(Client client) {
        Session session = sessionFactory.getCurrentSession();

        Client existingClient = session.get(Client.class, client.getClientId());
        existingClient.setIdClient(client.getClientId());
        existingClient.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
        existingClient.setAuthorizedGrantTypes(client.getAuthorizedGrantTypes());
        existingClient.setClientSecret(client.getClientSecret());
        existingClient.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
        existingClient.setRegisteredRedirectUri(client.getRegisteredRedirectUri());
        existingClient.setResourceIds(client.getResourceIds());
        existingClient.setScope(client.getScope());
        existingClient.setScoped(client.isScoped());
        existingClient.setSecretRequired(client.isSecretRequired());

        session.save(existingClient);
        logger.trace("{} client was edited in database", existingClient.getClientId());

    }
}
