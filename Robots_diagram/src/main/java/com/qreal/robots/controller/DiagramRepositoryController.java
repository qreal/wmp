/*
 * Copyright Vladimir Zakharov
 * Copyright Anastasia Kornilova
 * Copyright Lidiya Chernigovskaya
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

import com.qreal.robots.model.diagram.Diagram;
import com.qreal.robots.model.diagram.DiagramRequest;
import com.qreal.robots.model.diagram.Folder;
import com.qreal.robots.model.diagram.OpenRequest;
import com.qreal.robots.service.DiagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by vladzx on 22.06.15.
 */
@Controller
public class DiagramRepositoryController {

    @Autowired
    private DiagramService diagramService;

    @ResponseBody
    @RequestMapping(value = "/saveDiagram", method = RequestMethod.POST)
    public Long saveDiagram(@RequestBody DiagramRequest diagramRequest) {
        return diagramService.saveDiagram(diagramRequest);
    }

    @ResponseBody
    @RequestMapping(value = "/openDiagram", method = RequestMethod.POST)
    public Diagram openDiagram(@RequestBody OpenRequest request) {
        return diagramService.openDiagram(request.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/updateDiagram", method = RequestMethod.POST)
    public void rewriteDiagram(@RequestBody Diagram diagram) {
        diagramService.rewriteDiagram(diagram);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteDiagram", method = RequestMethod.POST)
    public void deleteDiagram(@RequestBody OpenRequest request) {
        diagramService.deleteDiagram(request.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/createFolder", method = RequestMethod.POST)
    public Long createFolder(@RequestBody Folder folder) {
        return diagramService.createFolder(folder);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteFolder", method = RequestMethod.POST)
    public void deleteFolder(@RequestBody OpenRequest request) {
        diagramService.deleteFolder(request.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/getFolderTree", method = RequestMethod.GET)
    public Folder getFolderTree() {
        return diagramService.getFolderTree();
    }
}