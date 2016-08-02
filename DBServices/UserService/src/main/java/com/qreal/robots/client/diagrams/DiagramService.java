package com.qreal.robots.client.diagrams;

/**
 * DiagramDBService interface.
 */
public interface DiagramService {

    /**
     * Creates root folder for user with specified username.
     *
     * @param userName name of user root folder created for
     */
    void createRootFolder(String userName);

}
