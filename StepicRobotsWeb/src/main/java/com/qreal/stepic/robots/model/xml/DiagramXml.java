/*
 * Copyright Vladimir Zakharov
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

package com.qreal.stepic.robots.model.xml;

import org.w3c.dom.Document;

import java.util.List;
import java.util.UUID;

/**
 * Created by vladimir-zakharov on 24.03.16.
 */
public class DiagramXml {

    private UUID uuid;
    private Document rootIdXml;
    private List<DiagramElementXml> diagramElements;

    public DiagramXml(Document rootIdXml, List<DiagramElementXml> diagramElements) {
        this.uuid = UUID.randomUUID();
        this.rootIdXml = rootIdXml;
        this.diagramElements = diagramElements;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Document getRootIdXml() {
        return rootIdXml;
    }

    public List<DiagramElementXml> getDiagramElements() {
        return diagramElements;
    }

}
