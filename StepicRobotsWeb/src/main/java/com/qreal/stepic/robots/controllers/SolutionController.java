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

package com.qreal.stepic.robots.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.stepic.robots.checker.Checker;
import com.qreal.stepic.robots.checker.Compressor;
import com.qreal.stepic.robots.constants.PathConstants;
import com.qreal.stepic.robots.model.checker.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by vladimir-zakharov on 31.08.15.
 */
@Controller
public abstract class SolutionController {

    @Autowired
    protected MessageSource messageSource;

    protected Compressor compressor;

    protected Checker checker;

    public SolutionController() {
        compressor = new Compressor();
        checker = new Checker();
    }

    protected Description getDescription(String kit, String name, Locale locale) throws IOException {
        String descriptionPath = String.format("%s/trikKit%s/tasks/%s/description_%s.json",
                PathConstants.STEPIC_PATH, kit, name, locale);
        File descriptionFile = new File(descriptionPath);
        if (!descriptionFile.exists()) {
            descriptionFile = new File(String.format("%s/trikKit%s/tasks/%s/description_ru.json",
                    PathConstants.STEPIC_PATH, kit, name));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(descriptionFile, Description.class);
    }

}
