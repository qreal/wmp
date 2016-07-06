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

package com.qreal.stepic.robots.model.diagram;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by vladimir-zakharov on 31.10.14.
 */
public class DiagramElement implements Serializable {

    private String logicalId;
    private String graphicalId;
    private String graphicalParent;
    private String type;
    private Set<IdObject> logicalChildren;
    private Set<IdObject> graphicalChildren;
    private Set<IdObject> logicalLinksIds;
    private Set<IdObject> graphicalLinksIds;
    private Set<Property> logicalProperties;
    private Set<Property> graphicalProperties;
    private Set<IdObject> incomingExplosions;

    public DiagramElement() {
    }

    public DiagramElement(String logicalId, String type, Set<Property> logicalProperties) {
        this.logicalId = logicalId;
        this.type = type;
        this.logicalProperties = logicalProperties;
    }

    public DiagramElement(String logicalId, String graphicalId, String type,
                          Set<Property> logicalProperties, Set<Property> graphicalProperties) {
        this.logicalId = logicalId;
        this.graphicalId = graphicalId;
        this.type = type;
        this.logicalProperties = logicalProperties;
        this.graphicalProperties = graphicalProperties;
    }

    public String getLogicalId() {
        return logicalId;
    }

    public void setLogicalId(String logicalId) {
        this.logicalId = logicalId;
    }

    public String getGraphicalId() {
        return graphicalId;
    }

    public void setGraphicalId(String graphicalId) {
        this.graphicalId = graphicalId;
    }

    public String getGraphicalParent() {
        return graphicalParent;
    }

    public void setGraphicalParent(String graphicalParent) {
        this.graphicalParent = graphicalParent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<IdObject> getLogicalChildren() {
        return logicalChildren;
    }

    public void setLogicalChildren(Set<IdObject> logicalChildren) {
        this.logicalChildren = logicalChildren;
    }

    public Set<IdObject> getGraphicalChildren() {
        return graphicalChildren;
    }

    public void setGraphicalChildren(Set<IdObject> graphicalChildren) {
        this.graphicalChildren = graphicalChildren;
    }

    public Set<IdObject> getLogicalLinksIds() {
        return logicalLinksIds;
    }

    public void setLogicalLinksIds(Set<IdObject> logicalLinksIds) {
        this.logicalLinksIds = logicalLinksIds;
    }

    public Set<IdObject> getGraphicalLinksIds() {
        return graphicalLinksIds;
    }

    public void setGraphicalLinksIds(Set<IdObject> graphicalLinksIds) {
        this.graphicalLinksIds = graphicalLinksIds;
    }

    public Set<Property> getLogicalProperties() {
        return logicalProperties;
    }

    public void setLogicalProperties(Set<Property> logicalProperties) {
        this.logicalProperties = logicalProperties;
    }

    public Set<Property> getGraphicalProperties() {
        return graphicalProperties;
    }

    public void setGraphicalProperties(Set<Property> graphicalProperties) {
        this.graphicalProperties = graphicalProperties;
    }

    public Set<IdObject> getIncomingExplosions() {
        return incomingExplosions;
    }

    public void setIncomingExplosions(Set<IdObject> incomingExplosions) {
        this.incomingExplosions = incomingExplosions;
    }

}
