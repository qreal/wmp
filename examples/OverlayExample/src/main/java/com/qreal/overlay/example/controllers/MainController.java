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

package com.qreal.overlay.example.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.qreal.overlay.example.loaders.TypesLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Created by vladimir-zakharov on 27.02.16.
 */
@Controller
public class MainController {

    private TypesLoader typesLoader;

    public MainController() {
        this.typesLoader = new TypesLoader();
    }

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("editor");
    }

    @ResponseBody
    @RequestMapping(value = "getTypes/", method = RequestMethod.POST)
    public JsonNode getTypes(@RequestParam(value="kit") String kit) throws IOException {
        return typesLoader.getTypesJson();
    }

}
