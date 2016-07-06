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

package com.qreal.robots.dao;

import com.qreal.robots.model.diagram.Diagram;
import com.qreal.robots.model.diagram.DiagramRequest;
import com.qreal.robots.model.diagram.Folder;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by vladzx on 07.11.14.
 */
@Repository
public class DiagramDAOImpl implements DiagramDAO {
    private static final Logger LOG = Logger.getLogger(DiagramDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public DiagramDAOImpl() {
    }

    public DiagramDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Long saveDiagram(DiagramRequest diagramRequest) {
        LOG.debug("saving diagram");
        Session session = sessionFactory.getCurrentSession();
        Folder folder = (Folder) session.get(Folder.class, diagramRequest.getFolderId());

        folder.getDiagrams().add(diagramRequest.getDiagram());
        session.update(folder);
        session.flush();
        return diagramRequest.getDiagram().getDiagramId();
    }

    public Diagram openDiagram(Long diagramId) {
        Session session = sessionFactory.getCurrentSession();
        List<Diagram> diagrams = session.createQuery("from Diagram where diagramId=:diagramId")
                .setParameter("diagramId", diagramId)
                .list();

        return diagrams.get(0);
    }

    public void rewriteDiagram(Diagram diagram) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(diagram);
    }

    public void deleteDiagram(Long diagramId) {
        Session session = sessionFactory.getCurrentSession();
        Diagram diagram = (Diagram) session.get(Diagram.class, diagramId);
        session.delete(diagram);
    }

    public Long createFolder(Folder folder) {
        LOG.debug("creating folder");
        Session session = sessionFactory.getCurrentSession();
        session.save(folder);
        return folder.getFolderId();
    }

    public void deleteFolder(Long folderId) {
        Session session = sessionFactory.getCurrentSession();
        Folder folder = (Folder) session.get(Folder.class, folderId);
        session.delete(folder);
    }

    public Folder getFolderTree(String userName) {
        Session session = sessionFactory.getCurrentSession();

        List<Folder> rootFolders = session.createQuery("from Folder where folderName=:folderName and userName=:userName")
                .setParameter("folderName", "root")
                .setParameter("userName", userName)
                .list();

        return rootFolders.get(0);
    }
}
