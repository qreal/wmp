package com.qreal.wmp.db.user.client.diagrams;

/** DiagramDBService interface.*/
public interface DiagramService {

    /**
     * Creates root folder for user with specified username.
     *
     * @param userName name of user root folder created for
     */
    void createRootFolder(String userName);

}
