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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladimir-zakharov on 18.09.15.
 */
public class RussianTranslator extends Translator {

    public RussianTranslator() {
        Map<String, String> translations = new HashMap<>();
        translations.put("истина", "true");
        translations.put("ложь", "false");
        translations.put("тело цикла", "iteration");
        appendTranslationStrings(translations);
    }

}
