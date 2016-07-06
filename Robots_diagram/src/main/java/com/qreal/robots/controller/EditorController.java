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

package com.qreal.robots.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.qreal.robots.loaders.TypesLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by vladzx on 25.10.14.
 */
@Controller
public class EditorController {

    private TypesLoader typesLoader;

    public EditorController() {
        this.typesLoader = new TypesLoader();
    }

    @RequestMapping(value = "/editor", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("editor/editor");
    }

    @ResponseBody
    @RequestMapping(value = "getTypes/", method = RequestMethod.POST)
    public JsonNode getTypes(@RequestParam(value="kit") String kit) throws IOException {
        return typesLoader.getTypesJson();
    }
}
