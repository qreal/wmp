package com.qreal.wmp.db.user.client.diagrams;

import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import org.apache.thrift.TException;

/** DiagramDBService interface. */
public interface DiagramService {

    /**
     * Creates root folder for a user with specified username.
     * @param userName name of user root folder created for
     */
    void createRootFolder(String userName) throws AbortedException, ErrorConnectionException, TException;

}
