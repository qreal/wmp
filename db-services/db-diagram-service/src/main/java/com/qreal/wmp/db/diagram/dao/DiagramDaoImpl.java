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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class DiagramDaoImpl implements DiagramDao {
    private static final Logger logger = LoggerFactory.getLogger(DiagramDaoImpl.class);

    private final SessionFactory sessionFactory;

    public DiagramDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Gets a folder from local DB using Hibernate ORM.
     * @param folderId Id of a folder to find
     */
    @Override
    public @NotNull Folder getFolder(Long folderId) throws NotFoundException {
        logger.trace("getFolder() was called with parameters: folderID = {}", folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder result = (Folder) session.get(Folder.class, folderId);
        logger.trace("getFolder() extracted result from session and returned it successfully.");
        if (result == null) {
            throw new NotFoundException(String.valueOf(folderId), "Folder with specified Id not found.");
        }
        return result;
    }

    /**
     * Saves a diagram to local DB using Hibernate ORM.
     * @param diagram  diagram to save (Id must not be set)
     * @param folderId id of a folder to save the diagram into
     */
    @Override
    public Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws AbortedException {
        logger.trace("saveDiagram() called with parameters: diagram = {}, folderID = {}", diagram.getName(), folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder folder;
        try {
            session.save(diagram);

            folder = getFolder(folderId);
            folder.getDiagrams().add(diagram);

            updateFolder(folder);
        } catch (NotFoundException e) {
            logger.error("Got null folder object for folder id {}.", folderId);
            throw new AbortedException("Folder to save diagram into not found", "saveDiagram safely aborted",
                    DiagramDaoImpl.class.getName(), e);
        }
        logger.trace("saveDiagram() successfully saved diagram {}.", diagram.getName());
        return diagram.getId();
    }

    /**
     * Gets diagrams from local DB using Hibernate ORM.
     * @param diagramId id of diagram to find
     */
    @Override
    public @NotNull Diagram getDiagram(Long diagramId) throws NotFoundException {
        logger.trace("getDiagram() was called with parameters: id = {}.", diagramId);
        Session session = sessionFactory.getCurrentSession();
        Diagram diagram = (Diagram) session.get(Diagram.class, diagramId);
        if (diagram == null) {
            throw new NotFoundException(String.valueOf(diagramId), "Diagram with specified Id not found.");
        }
        logger.trace("getDiagram() extracted a diagram successfully.");
        return diagram;
    }

    /** Checks whether a diagram with specified Id exists. */
    @Override
    public boolean isExistsDiagram(Long diagramId) {
        logger.trace("isExistsDiagram() called with parameters: id = {}", diagramId);
        Session session = sessionFactory.getCurrentSession();
        Diagram diagram = (Diagram) session.get(Diagram.class, diagramId);
        return diagram != null;
    }

    /**
     * Rewrites a diagram at local DB using Hibernate ORM.
     * @param diagram diagram to rewrite (Id must be set correctly).
     */
    @Override
    public void rewriteDiagram(@NotNull Diagram diagram) throws AbortedException {
        logger.trace("updateDiagram() was called with parameters: diagram = {}.", diagram.getName());
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsDiagram(diagram.getId())) {
            throw new AbortedException("Diagram with specified Id doesn't exist. Use save instead.",
                    "updateDiagram() safely aborted.", DiagramDaoImpl.class.getName());
        }
        session.merge(diagram);
        logger.trace("updateDiagram() successfully edited diagram {}.", diagram.getName());
    }

    /**
     * Deletes a diagram from local DB using Hibernate ORM.
     * Possible foreign key to user (in case of root folder) will be ignored.
     * @param diagramId id of a diagram to delete
     */
    @Override
    public void deleteDiagram(Long diagramId) throws AbortedException {
        logger.trace("deleteDiagram() was called with parameters: id = {}.", diagramId);
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsDiagram(diagramId)) {
            throw new AbortedException("Diagram with specified Id doesn't exist.", "deleteDiagram() safely aborted.",
                    DiagramDaoImpl.class.getName());
        }
        Diagram diagram = (Diagram) session.get(Diagram.class, diagramId);
        session.delete(diagram);
        logger.trace("deleteDiagram() deleted diagram with id {}", diagramId);
    }

    /**
     * Saves a folder at local DB using Hibernate ORM.
     *
     * @param folder folder to create (Id must not be set)
     */
    @Override
    public Long saveFolder(@NotNull Folder folder) throws AbortedException {
        logger.trace("saveFolder() was called with parameters: folder = {}.", folder.getFolderName());
        Session session = sessionFactory.getCurrentSession();

        session.save(folder);
        for (Folder dir : folder.getParentFolders()) {
            dir.getChildrenFolders().add(folder);
            updateFolder(dir);
        }

        logger.trace("saveFolder() successfully created a folder with id {}", folder.getId());
        return folder.getId();
    }

    /** Checks whether a folder with specified Id exists.*/
    @Override
    public boolean isExistsFolder(Long folderId) {
        logger.trace("isExistsFolder() was called with parameters: id = {}.", folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder folder = (Folder) session.get(Folder.class, folderId);
        return folder != null;
    }

    @Override
    public void updateFolder(@NotNull Folder folder) throws AbortedException {
        logger.trace("updateFolder() was called with parameters: folder = {}.", folder.getFolderName());
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsFolder(folder.getId())) {
            throw new AbortedException("Folder with specified Id doesn't exist.", "updateFolder() safely aborted.",
                    DiagramDaoImpl.class.getName());
        }
        session.merge(folder);

        logger.trace("updateFolder() successfully updated folder {}.", folder.getFolderName());
    }

    /**
     * Deletes a folder from local DB using Hibernate ORM.
     * @param folderId id of folder to delete
     */
    @Override
    public void deleteFolder(Long folderId) throws AbortedException {
        logger.trace("deleteFolder() was called with parameters: folderId = {}.", folderId);
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsFolder(folderId)) {
            throw new AbortedException("Folder with specified Id doesn't exist.", "deleteFolder safely aborted.",
                    DiagramDaoImpl.class.getName());
        }
        Folder folder = (Folder) session.get(Folder.class, folderId);
        session.delete(folder);

        logger.trace("deleteFolder() successfully deleted a folder with id {}", folderId);
    }

    /**
     * Returns user's root folder.
     * @param userName name of a user which root folder will be deleted
     */
    @Override
    @NotNull
    public Folder getFolderTree(String userName) throws NotFoundException {
        logger.trace("getFolderTree() called with parameters: owners = {}", userName);
        Session session = sessionFactory.getCurrentSession();

        List<Folder> rootFolders = session.createQuery("select c from Folder c where" +
                "(c.folderName = :folderName and :userName in elements(c.owners))").
                setParameter("folderName", "root").setParameter("userName", userName).list();
        logger.trace("getFolderTree() extracted list of results from session with {} elements. First one will be" +
                " returned.", rootFolders.size());
        if (rootFolders.isEmpty()) {
            throw new NotFoundException(String.valueOf(userName), "FolderTree for user with specified username not" +
                    " found.");
        }
        return rootFolders.get(0);
    }
}
