/*
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

package com.qreal.robots.model.diagram;

import java.io.Serializable;

/**
 * Created by korniluk13 on 14.07.2015.
 */

public class DiagramRequest implements Serializable {

    private Diagram diagram;

    private Long folderId;

    public Diagram getDiagram() {
        return this.diagram;
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
}
