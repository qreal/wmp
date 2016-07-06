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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.stepic.robots.constants.PathConstants;
import com.qreal.stepic.robots.model.checker.TaskItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by vladimir-zakharov on 13.08.15.
 */

@Controller
public class TasksController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "redirect:/tasks";
    }

    @RequestMapping(value = "checker/tasks", method = RequestMethod.GET)
    public String oldAddress() {
        return "redirect:/tasks";
    }

    @RequestMapping(value = "tasks", method = RequestMethod.GET)
    public ModelAndView kitsHandler(Locale locale) {
        ModelAndView modelAndView = new ModelAndView("checker/kits");
        return modelAndView;
    }

    @RequestMapping(value = "tasks", params = { "kit" }, method = RequestMethod.GET)
    public ModelAndView tasksHandler(Locale locale, @RequestParam(value="kit") String kit)
        throws IOException {
        ModelAndView modelAndView = new ModelAndView("checker/tasks");

        List<TaskItem> taskItems = this.parseTaskList(new File(PathConstants.STEPIC_PATH + "/list.json"));

        File tasksDir = new File(String.format("%s/trikKit%s/tasks", PathConstants.STEPIC_PATH, kit));
        Set<String> taskIds = new HashSet<>();
        Arrays.asList(tasksDir.listFiles()).forEach(task -> taskIds.add(task.getName()));

        List<TaskItem> tasks = new ArrayList();
        taskItems.forEach(item -> tasks.add(item));

        modelAndView.addObject("tasks", tasks);
        Map<String, String> taskNames = parseTaskNames(new File(String.format("%s/names_%s.json",
                PathConstants.STEPIC_PATH, locale)), locale);
        modelAndView.addObject("taskNames", taskNames);
        modelAndView.addObject("kit", kit);

        return modelAndView;
    }

    private List<TaskItem> parseTaskList(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, new TypeReference<List<TaskItem>>() {});
    }

    private Map<String, String> parseTaskNames(File file, Locale locale) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> taskNames = objectMapper.readValue(file, Map.class);
        return taskNames;
    }
}
