package com.qreal.robots.components.database.diagrams.DAO;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Component("diagramDAO")
@Transactional
public class DiagramDAOImpl implements DiagramDAO {

    private static final Logger logger = LoggerFactory.getLogger(DiagramDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public DiagramDAOImpl() {
    }

    public DiagramDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Folder getFolder(Long folderId) {
        logger.trace("getFolder method called with parameters: folderID = {}", folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder result = (Folder) session.get(Folder.class, folderId);
        logger.trace("getFolder method extracted result from session and return it");
        return result;
    }

    @Override
    public Long saveDiagram(Diagram diagram, Long folderId) {
        logger.trace("saveDiagram method called with parameters: diagram = {}, folderID = {}", diagram.getName(),
                folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder folder = getFolder(folderId);
        folder.getDiagrams().add(diagram);
        session.update(folder);
        session.flush();
        logger.trace("saveDiagram method saved diagram {}", diagram.getName());
        return diagram.getId();
    }

    @Override
    public Diagram openDiagram(Long diagramId) {
        logger.trace("openDiagram method called with parameters: diagramId = {}", diagramId);
        Session session = sessionFactory.getCurrentSession();
        List<Diagram> diagrams = session.createQuery("from Diagram where diagramId=:diagramId").
                setParameter("diagramId", diagramId).list();
        logger.trace("openDiagram method extracted list of results from session with {} elements. First will be " +
                "returned.", diagrams.size());
        return diagrams.stream().findFirst().orElse(null);
    }

    @Override
    public void rewriteDiagram(Diagram diagram) {
        logger.trace("rewriteDiagram method called with parameters: diagram = {}", diagram.getName());
        Session session = sessionFactory.getCurrentSession();
        session.merge(diagram);
        logger.trace("rewriteDiagram method edited diagram {}", diagram.getName());
    }

    @Override
    public void deleteDiagram(Long diagramId) {
        logger.trace("deleteDiagram method called with parameters: diagramId = {}", diagramId);
        Session session = sessionFactory.getCurrentSession();
        Diagram diagram = (Diagram) session.get(Diagram.class, diagramId);
        session.delete(diagram);
        logger.trace("deleteDiagram method deleted diagram with id {}", diagramId);
    }

    @Override
    public Long createFolder(Folder folder) {
        logger.trace("createFolder method called with parameters: folder = {}", folder.getFolderName());
        Session session = sessionFactory.getCurrentSession();
        session.save(folder);
        Long folderId = folder.getId();
        logger.trace("createFolder method created folder with id {}", folderId);
        return folderId;
    }

    @Override
    public void deleteFolder(Long folderId) {
        logger.trace("deleteFolder method called with parameters: folderId = {}", folderId);
        Session session = sessionFactory.getCurrentSession();
        Folder folder = (Folder) session.get(Folder.class, folderId);
        session.delete(folder);
        logger.trace("deleteFolder method deleted folder with id {}", folderId);
    }

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