package com.qreal.robots.dao;

import com.qreal.robots.model.Diagram;
import com.qreal.robots.model.Folder;
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

/**
 * Implementation of Diagram DAO.
 * Visibility level: package.
 */
@Repository
@Component("diagramDao")
@Transactional
class DiagramDaoImpl implements DiagramDao {

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
    public Folder getFolder(Long folderId) {
        logger.trace("getFolder method called with parameters: folderID = {}", folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder result = (Folder) session.get(Folder.class, folderId);
        logger.trace("getFolder method extracted result from session and return it");
        return result;
    }

    /**
     * Saves diagram to local DB using Hibernate ORM.
     *
     * @param diagram  diagram to save (Id must not be set)
     * @param folderId id of folder to save diagram in
     */
    @Override
    public Long saveDiagram(@NotNull Diagram diagram, Long folderId) {
        logger.trace("saveDiagram method called with parameters: diagram = {}, folderID = {}", diagram.getName(),
                folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder folder = getFolder(folderId);
        if (folder == null) {
            logger.error("Got null folder object for folder id {}.", folderId);
            return 0L; // FIXME
        }

        List<Diagram> diagrams = folder.getDiagrams();
        if (diagrams == null) {
            logger.error("Got empty diagrams list for folder id {}.", folderId);
            return 0L; // FIXME
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
    public Diagram openDiagram(Long diagramId) {
        logger.trace("openDiagram method called with parameters: id = {}", diagramId);
        Session session = sessionFactory.getCurrentSession();
        List<Diagram> diagrams = session.createQuery("from Diagram where id=:diagramId").
                setParameter("diagramId", diagramId).list();
        logger.trace("openDiagram method extracted list of results from session with {} elements. First will be " +
                "returned.", diagrams.size());
        return diagrams.stream().findFirst().orElse(null);
    }

    /**
     * Rewrites diagram at local DB using Hibernate ORM.
     *
     * @param diagram diagram to rewrite (<code>diagram.id</code> must be set correctly).
     */
    @Override
    public void rewriteDiagram(@NotNull Diagram diagram) {
        logger.trace("rewriteDiagram method called with parameters: diagram = {}", diagram.getName());
        Session session = sessionFactory.getCurrentSession();
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
    public void deleteDiagram(Long diagramId) {
        logger.trace("deleteDiagram method called with parameters: id = {}", diagramId);
        Session session = sessionFactory.getCurrentSession();
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
    public Long createFolder(@NotNull Folder folder) {
        logger.trace("createFolder method called with parameters: folder = {}", folder.getFolderName());
        Session session = sessionFactory.getCurrentSession();
        session.save(folder);
        Long folderId = folder.getId();
        logger.trace("createFolder method created folder with id {}", folderId);
        return folderId;
    }

    /**
     * Deletes folder from local DB using Hibernate ORM.
     *
     * @param folderId id of folder to delete
     */
    @Override
    public void deleteFolder(Long folderId) {
        logger.trace("deleteFolder method called with parameters: folderId = {}", folderId);
        Session session = sessionFactory.getCurrentSession();
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
    public Folder getFolderTree(String userName) {
        logger.trace("getFolderTree method called with parametrs: userName = {}", userName);
        Session session = sessionFactory.getCurrentSession();

        List<Folder> rootFolders = session.createQuery("from Folder where folderName=:folderName and " +
                "userName=:userName").setParameter("folderName", "root").setParameter("userName", userName).list();
        logger.trace("getFolderTree method extracted list of results from session with {} elements. First will be " +
                "returned.", rootFolders.size());
        return rootFolders.stream().findFirst().orElse(null);
    }
}
