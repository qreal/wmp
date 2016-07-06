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

package com.qreal.stepic.robots.model.xml.builders;

/**
 * Created by vladimir-zakharov on 25.03.16.
 */
public class IdObjectXmlBuilder {

    private static final String idTypeTemplate = "<object id=\"qrm:/RobotsMetamodel/RobotsDiagram/%s/{%s}\"/>";
    private static final String fullIdTemplate = "<object id=\"%s\"/>";

    public static String buildXmlString(String id, String type) {
        return String.format(idTypeTemplate, type, id);
    }

    public static String buildXmlString(String fullId) {
        return String.format(fullIdTemplate, fullId);
    }

}
