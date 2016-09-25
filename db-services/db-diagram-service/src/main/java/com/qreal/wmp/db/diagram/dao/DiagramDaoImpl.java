package com.qreal.wmp.db.diagram.dao;

import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.db.diagram.model.Diagram;
import com.qreal.wmp.db.diagram.model.Folder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Implementation of Diagram DAO.
 * Visibility level: package.
 */
@Repository
@Component("diagramDao")
@Transactional
public class DiagramDaoImpl implements DiagramDao {

    private static final Logger logger = LoggerFactory.getLogger(DiagramDaoImpl.class);

    private final SessionFactory sessionFactory;

    @Autowired
    public DiagramDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Gets folder from local DB using Hibernate ORM.
     *
     * @param folderId id of folder to find
     */
    @Override
    @NotNull
    public Folder getFolder(Long folderId) throws NotFoundException {
        logger.trace("getFolder method called with parameters: folderID = {}", folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder result = (Folder) session.get(Folder.class, folderId);
        logger.trace("getFolder method extracted result from session and return it");
        if (result == null) {
            throw new NotFoundException(String.valueOf(folderId), "Folder with specified Id not found.");
        }
        return result;
    }

    /**
     * Saves diagram to local DB using Hibernate ORM.
     *
     * @param diagram  diagram to save (Id must not be set)
     * @param folderId id of folder to save diagram in
     */
    @Override
    public Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws AbortedException {
        logger.trace("saveDiagram method called with parameters: diagram = {}, folderID = {}", diagram.getName(),
                folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder folder = null;
        Set<Diagram> diagrams = null;
        try {
            folder = getFolder(folderId);
            diagrams = folder.getDiagrams();
        } catch (NotFoundException e) {
            logger.error("Got null folder object for folder id {}.", folderId);
            throw new AbortedException("Folder to save diagram into not found", "saveDiagram safely aborted",
                    DiagramDaoImpl.class.getName(), e);
        }
        diagrams.add(diagram);
        session.update(folder);
        session.flush();
        logger.trace("saveDiagram method saved diagram {}", diagram.getName());
        return diagram.getId();
    }

    /**
     * Gets diagrams from local DB using Hibernate ORM.
     *
     * @param diagramId id of diagram to find
     */
    @Override
    @NotNull
    public Diagram getDiagram(Long diagramId) throws NotFoundException {
        logger.trace("getDiagram method called with parameters: id = {}", diagramId);
        Session session = sessionFactory.getCurrentSession();
        Diagram diagram = (Diagram) session.get(Diagram.class, diagramId);
        if (diagram == null) {
            throw new NotFoundException(String.valueOf(diagramId), "Diagram with specified Id not found.");
        }
        logger.trace("getDiagram method extracted diagram");
        return diagram;
    }

    /** Checks whether diagram with specified id exists.*/
    @Override
    public boolean isExistsDiagram(Long diagramId) {
        logger.trace("isExistsDiagram method called with parameters: id = {}", diagramId);
        Session session = sessionFactory.getCurrentSession();
        Diagram diagram = (Diagram) session.get(Diagram.class, diagramId);
        return diagram != null;
    }

    /**
     * Rewrites diagram at local DB using Hibernate ORM.
     *
     * @param diagram diagram to rewrite (Id must be set correctly).
     */
    @Override
    public void rewriteDiagram(@NotNull Diagram diagram) throws AbortedException {
        logger.trace("rewriteDiagram method called with parameters: diagram = {}", diagram.getName());
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsDiagram(diagram.getId())) {
            throw new AbortedException("Diagram with specified Id doesn't exist. Use save instead.",
                    "rewriteDiagram safely aborted.", DiagramDaoImpl.class.getName());
        }
        session.merge(diagram);
        logger.trace("rewriteDiagram method edited diagram {}", diagram.getName());
    }

    /**
     * Deletes diagram from local DB using Hibernate ORM.
     * Possible foreign key to user (in case of root folder) will be ignored.
     *
     * @param diagramId id of diagram to delete
     */
    @Override
    public void deleteDiagram(Long diagramId) throws AbortedException {
        logger.trace("deleteDiagram method called with parameters: id = {}", diagramId);
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsDiagram(diagramId)) {
            throw new AbortedException("Diagram with specified Id doesn't exist.", "deleteDiagram safely aborted.",
                    DiagramDaoImpl.class.getName());
        }
        Diagram diagram = (Diagram) session.get(Diagram.class, diagramId);
        session.delete(diagram);
        logger.trace("deleteDiagram method deleted diagram with id {}", diagramId);
    }

    /**
     * Saves folder at local DB using Hibernate ORM.
     *
     * @param folder folder to create (Id must not be set)
     */
    @Override
    public Long saveFolder(@NotNull Folder folder) {
        logger.trace("saveFolder method called with parameters: folder = {}", folder.getFolderName());
        Session session = sessionFactory.getCurrentSession();
        session.save(folder);
        Long folderId = folder.getId();
        logger.trace("saveFolder method created folder with id {}", folderId);
        return folderId;
    }

    /** Checks whether folder with specified id exists.*/
    @Override
    public boolean isExistsFolder(Long folderId) {
        logger.trace("isExistsFolder method called with parameters: id = {}", folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder folder = (Folder) session.get(Folder.class, folderId);
        return folder != null;
    }

    /**
     * Deletes folder from local DB using Hibernate ORM.
     *
     * @param folderId id of folder to delete
     */
    @Override
    public void deleteFolder(Long folderId) throws AbortedException {
        logger.trace("deleteFolder method called with parameters: folderId = {}", folderId);
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsFolder(folderId)) {
            throw new AbortedException("Folder with specified Id doesn't exist.", "deleteFolder safely aborted.",
                    DiagramDaoImpl.class.getName());
        }
        Folder folder = (Folder) session.get(Folder.class, folderId);
        session.delete(folder);
        logger.trace("deleteFolder method deleted folder with id {}", folderId);
    }

    /**
     * Return root folder of user.
     *
     * @param userName name of user which root folder seeking
     */
    @Override
    @NotNull
    public Folder getFolderTree(String userName) throws NotFoundException {
        logger.trace("getFolderTree method called with parametrs: userName = {}", userName);
        Session session = sessionFactory.getCurrentSession();

        List<Folder> rootFolders = session.createQuery("from Folder where folderName=:folderName and " +
                "userName=:userName").setParameter("folderName", "root").setParameter("userName", userName).list();
        logger.trace("getFolderTree method extracted list of results from session with {} elements. First will be " +
                "returned.", rootFolders.size());
        if (rootFolders.isEmpty()) {
            throw new NotFoundException(String.valueOf(userName), "FolderTree for user with specified username not " +
                    "found.");
        }
        return rootFolders.get(0);
    }
}
