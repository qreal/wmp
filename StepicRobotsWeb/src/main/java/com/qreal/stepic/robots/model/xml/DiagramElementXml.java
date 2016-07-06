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

/**
 * Created by vladimir-zakharov on 24.03.16.
 */
public class DiagramElementXml {

    private String logicalId;
    private String graphicalId;
    private String type;
    private Document logicalPart;
    private Document graphicalPart;

    public DiagramElementXml(String logicalId, String graphicalId, String type,
                             Document logicalPart, Document graphicalPart) {
        this.logicalId = logicalId;
        this.graphicalId = graphicalId;
        this.type = type;
        this.logicalPart = logicalPart;
        this.graphicalPart = graphicalPart;
    }

    public String getLogicalId() {
        return logicalId;
    }

    public String getGraphicalId() {
        return graphicalId;
    }

    public String getType() {
        return type;
    }

    public Document getLogicalPart() {
        return logicalPart;
    }

    public Document getGraphicalPart() {
        return graphicalPart;
    }
}
