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

package com.qreal.stepic.robots.translators;

import com.qreal.stepic.robots.model.diagram.DiagramElement;
import com.qreal.stepic.robots.model.diagram.Property;

import java.util.Locale;
import java.util.Set;

/**
 * Created by vladimir-zakharov on 18.09.15.
 */
public class PropertyValueTranslator {

    public void translateAllPropertiesValue(Set<DiagramElement> nodes, Locale locale) {
        TranslatorFactory factory = new TranslatorFactory();
        Translator translator = factory.createTranslator(locale);
        for (DiagramElement node : nodes) {
            translatePropertiesValue(node.getLogicalProperties(), translator);
        }
    }

    private void translatePropertiesValue(Set<Property> properties, Translator translator) {
        for (Property property : properties) {
            if (translator.isPredefindesValue(property.getValue())) {
                property.setValue(translator.translate(property.getValue()));
            }
        }
    }

}
