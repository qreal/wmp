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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by vladimir-zakharov on 05.11.14.
 */
public class Diagram implements Serializable {

    private Set<DiagramElement> nodes;
    private Set<DiagramElement> links;

    @JsonCreator
    public Diagram(@JsonProperty("nodes") Set<DiagramElement> nodes, @JsonProperty("links") Set<DiagramElement> links) {
        this.nodes = nodes;
        this.links = links;
    }

    public Set<DiagramElement> getNodes() {
        return nodes;
    }

    public void setNodes(Set<DiagramElement> nodes) {
        this.nodes = nodes;
    }

    public Set<DiagramElement> getLinks() {
        return links;
    }

    public void setLinks(Set<DiagramElement> links) {
        this.links = links;
    }

    public void addNodes(Set<DiagramElement> nodes) {
        this.nodes.addAll(nodes);
    }

    public void addLinks(Set<DiagramElement> links) {
        this.links.addAll(links);
    }

}
