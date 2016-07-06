/*
 * Copyright Vladimir Zakharov
 * Copyright Anastasia Kornilova
 * Copyright Lidiya Chernigovskaya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.robots.service;

import com.qreal.robots.dao.DiagramDAO;
import com.qreal.robots.model.diagram.Diagram;
import com.qreal.robots.model.diagram.DiagramRequest;
import com.qreal.robots.model.diagram.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vladzx on 22.06.15.
 */
@Service
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