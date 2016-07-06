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

import java.util.Set;

/**
 * Created by vladimir-zakharov on 21.03.16.
 */
public class ElementGraphicalPart {

    private String id;
    private String logicalId;
    private String type;
    private Set<Property> properties;

    public ElementGraphicalPart(String id, String logicalId, String type, Set<Property> properties) {
        this.id = id;
        this.logicalId = logicalId;
        this.type = type;
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public String getLogicalId() {
        return logicalId;
    }

    public String getType() {
        return type;
    }

    public Set<Property> getProperties() {
        return properties;
    }

}
