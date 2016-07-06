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

package com.qreal.stepic.robots.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qreal.stepic.robots.constants.PathConstants;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by vladimir-zakharov on 07.09.15.
 */
public class TypesLoader {

    private ObjectMapper mapper;

    public TypesLoader() {
        mapper = new ObjectMapper();
    }

    public JsonNode getTypesJson(String kit, String name, Locale locale) throws IOException {
        ObjectNode resultTasksTypes = mapper.createObjectNode();

        JsonNode tasksTypes =  mapper.readTree(new File(String.format("%s/trikKit%s/tasks/%s/typesList.json",
                PathConstants.STEPIC_PATH, kit, name)));
        JsonNode allTypes = mapper.readTree(new File(String.format("%s/trikKit%s/elementsTypes_%s.json",
                PathConstants.STEPIC_PATH, kit, locale)));

        resultTasksTypes.set("elements", getElementsTypes(tasksTypes, allTypes));
        resultTasksTypes.set("blocks", getBlocksTypes(tasksTypes, allTypes));

        return resultTasksTypes;
    }

    private ArrayNode getElementsTypes(JsonNode tasksTypes, JsonNode allTypes) {
        JsonNode taskElements = tasksTypes.path("elements");
        JsonNode allElements = allTypes.path("elements");
        return getObjectsWithTypes(taskElements, allElements);
    }

    private ObjectNode getBlocksTypes(JsonNode tasksTypes, JsonNode allTypes) {
        ObjectNode resultBlocksNode = mapper.createObjectNode();
        JsonNode taskBlocks = tasksTypes.path("blocks");
        JsonNode allBlocks= allTypes.path("blocks");
        JsonNode categoriesNames = allTypes.path("categoriesNames");
        resultBlocksNode.set("general", getGeneralTypes(taskBlocks, allBlocks));
        resultBlocksNode.set("palette", getPaletteTypes(taskBlocks, allBlocks, categoriesNames));
        return resultBlocksNode;
    }

    private ArrayNode getGeneralTypes(JsonNode taskBlocksTypes, JsonNode allBlocksTypes) {
        JsonNode taskGeneralTypes = taskBlocksTypes.path("general");
        return getObjectsWithTypes(taskGeneralTypes, allBlocksTypes);
    }

    private ObjectNode getPaletteTypes(JsonNode taskBlocksTypes, JsonNode allBlocksTypes, JsonNode categoriesNames) {
        ObjectNode resultPaletteNode = mapper.createObjectNode();
        JsonNode taskPaletteTypes = taskBlocksTypes.path("palette");
        Iterator<Map.Entry<String, JsonNode>> categoriesIterator = taskPaletteTypes.fields();

        while (categoriesIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = categoriesIterator.next();
            String category = entry.getKey();
            JsonNode taskCategoryNode = taskPaletteTypes.path(category);
            ArrayNode categoryArray = getObjectsWithTypes(taskCategoryNode, allBlocksTypes);
            resultPaletteNode.set(categoriesNames.get(category).textValue(), categoryArray);
        }

        return resultPaletteNode;
    }

    private ArrayNode getObjectsWithTypes(JsonNode taskNode, JsonNode sourceNode) {
        ArrayNode array = mapper.createArrayNode();
        Iterator<JsonNode> typesIterator = taskNode.elements();

        while (typesIterator.hasNext()) {
            JsonNode type = typesIterator.next();
            String typeName = type.textValue();
            JsonNode typeObject = sourceNode.path(typeName);
            ((ObjectNode) typeObject).put("type", typeName);
            array.add(typeObject);
        }

        return array;
    }

}
