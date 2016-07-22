package com.qreal.robots.components.database.diagrams.DAO;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.DiagramRequest;
import com.qreal.robots.components.editor.model.diagram.Folder;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Component("DiagramDAO")
@Transactional
public class DiagramDAOImpl implements DiagramDAO {

    private static final Logger LOG = Logger.getLogger(DiagramDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public DiagramDAOImpl() {
    }

    public DiagramDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Folder getFolder(Long folderId) {
        Session session = sessionFactory.getCurrentSession();
        return (Folder) session.get(Folder.class, folderId);
    }

    @Override
    public Long saveDiagram(Diagram diagram, Long folderId) {
        Session session = sessionFactory.getCurrentSession();
        Folder folder = getFolder(folderId);
        folder.getDiagrams().add(diagram);
        session.update(folder);
        session.flush();
        return diagram.getDiagramId();
    }

    @Override
    public Diagram openDiagram(Long diagramId) {
        Session session = sessionFactory.getCurrentSession();
        List<Diagram> diagrams = session.createQuery("from Diagram where diagramId=:diagramId")
                .setParameter("diagramId", diagramId)
                .list();
        return diagrams.get(0);
    }

    @Override
    public void rewriteDiagram(Diagram diagram) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(diagram);
    }

    @Override
    public void deleteDiagram(Long diagramId) {
        Session session = sessionFactory.getCurrentSession();
        Diagram diagram = (Diagram) session.get(Diagram.class, diagramId);
        session.delete(diagram);
    }

    @Override
    public Long createFolder(Folder folder) {
        LOG.debug("creating folder");
        Session session = sessionFactory.getCurrentSession();
        session.save(folder);
        return folder.getFolderId();
    }

    @Override
    public void deleteFolder(Long folderId) {
        Session session = sessionFactory.getCurrentSession();
        Folder folder = (Folder) session.get(Folder.class, folderId);
        session.delete(folder);
    }

    @Override
    public Folder getFolderTree(String userName) {
        Session session = sessionFactory.getCurrentSession();

        List<Folder> rootFolders = session.createQuery("from Folder where folderName=:folderName and userName=:userName")
                .setParameter("folderName", "root")
                .setParameter("userName", userName)
                .list();
        return rootFolders.get(0);
    }
}