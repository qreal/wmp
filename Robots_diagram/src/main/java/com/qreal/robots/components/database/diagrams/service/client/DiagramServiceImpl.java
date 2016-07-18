package com.qreal.robots.components.database.diagrams.service.client;

import com.qreal.robots.components.database.diagrams.DAO.DiagramDAO;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.DiagramRequest;
import com.qreal.robots.components.editor.model.diagram.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("DiagramService")
public class DiagramServiceImpl implements DiagramService {

    @Autowired
    private DiagramDAO diagramDAO;

    @Transactional
    @Override
    public Long saveDiagram(DiagramRequest diagramRequest) {
        return diagramDAO.saveDiagram(diagramRequest);
    }

    @Transactional
    @Override
    public Diagram openDiagram(Long diagramId) {
        return diagramDAO.openDiagram(diagramId);
    }

    @Transactional
    @Override
    public void rewriteDiagram(Diagram diagram) {
        diagramDAO.rewriteDiagram(diagram);
    }

    @Transactional
    @Override
    public void deleteDiagram(Long diagramId) {
        diagramDAO.deleteDiagram(diagramId);
    }

    @Transactional
    @Override
    public void createRootFolder(String userName) {
        Folder rootFolder = new Folder("root", userName);
        diagramDAO.createFolder(rootFolder);
    }

    @Transactional
    @Override
    public Long createFolder(Folder folder) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        folder.setUserName(userName);
        return diagramDAO.createFolder(folder);
    }

    @Transactional
    @Override
    public void deleteFolder(Long folderId) {
        diagramDAO.deleteFolder(folderId);
    }

    @Transactional
    @Override
    public Folder getFolderTree() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return diagramDAO.getFolderTree(userName);
    }

}