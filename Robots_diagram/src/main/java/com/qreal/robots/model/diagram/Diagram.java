/*
 * Copyright Vladimir Zakharov
 * Copyright Denis Ageev
 * Copyright Anastasia Kornilova
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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by vladzx on 05.11.14.
 */
@Entity
@Table(name = "diagrams")
public class Diagram implements Serializable {

    @Id
    @Column(name = "diagram_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diagramId;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<DefaultDiagramNode> nodes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<Link> links;

    public Long getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(Long diagramId) {
        this.diagramId = diagramId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNodes(Set<DefaultDiagramNode> nodes) {
        this.nodes = nodes;
    }

    public Set<DefaultDiagramNode> getNodes() {
        return nodes;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public Set<Link> getLinks() {
        return links;
    }

}
