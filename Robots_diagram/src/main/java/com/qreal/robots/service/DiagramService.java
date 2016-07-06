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

import com.qreal.robots.model.diagram.Diagram;
import com.qreal.robots.model.diagram.DiagramRequest;
import com.qreal.robots.model.diagram.Folder;

/**
 * Created by vladzx on 22.06.15.
 */
public interface DiagramService {

    public Long saveDiagram(DiagramRequest diagramRequest);

    public Diagram openDiagram(Long diagramId);

    public void rewriteDiagram(Diagram diagram);

    public void deleteDiagram(Long diagramId);

    public void createRootFolder(String userName);

    public Long createFolder(Folder folder);

    public void deleteFolder(Long folderId);

    public Folder getFolderTree();

}
