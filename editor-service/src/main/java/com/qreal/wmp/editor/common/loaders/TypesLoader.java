package com.qreal.wmp.editor.common.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Component
public class TypesLoader {

    private final ObjectMapper mapper;

    public TypesLoader() {
        mapper = new ObjectMapper();
    }

    /** Provides JSON types for client.*/
    public JsonNode getTypesJson(String notation) throws IOException {
        ObjectNode resultTypes = mapper.createObjectNode();

        ClassLoader classLoader = getClass().getClassLoader();
        JsonNode typesList = mapper.readTree(
                new File(classLoader.getResource(notation + "/typesList.json").getFile()));
        JsonNode allTypes = mapper.readTree(
                new File(classLoader.getResource(notation + "/elementsTypes_en.json").getFile()));

        resultTypes.set("flows", getFlowsTypes(typesList, allTypes));
        resultTypes.set("blocks", getBlocksTypes(typesList, allTypes));

        return resultTypes;
    }

    private ArrayNode getFlowsTypes(JsonNode typesList, JsonNode allTypes) {
        JsonNode listElements = typesList.path("flows");
        JsonNode allElements = allTypes.path("flows");
        return getObjectsWithTypes(listElements, allElements);
    }

    private ObjectNode getBlocksTypes(JsonNode typesList, JsonNode allTypes) {
        JsonNode listElements = typesList.path("blocks");
        JsonNode allBlocks = allTypes.path("blocks");
        JsonNode categoriesNames = allTypes.path("categoriesNames");
        return getPaletteTypes(listElements, allBlocks, categoriesNames);
    }

    private ObjectNode getPaletteTypes(JsonNode listBlocksTypes, JsonNode allBlocksTypes, JsonNode categoriesNames) {
        ObjectNode resultPaletteNode = mapper.createObjectNode();
        Iterator<Map.Entry<String, JsonNode>> categoriesIterator = listBlocksTypes.fields();

        while (categoriesIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = categoriesIterator.next();
            String category = entry.getKey();
            JsonNode taskCategoryNode = listBlocksTypes.path(category);
            ArrayNode categoryArray = getObjectsWithTypes(taskCategoryNode, allBlocksTypes);
            resultPaletteNode.set(categoriesNames.get(category).textValue(), categoryArray);
        }

        return resultPaletteNode;
    }

    private ArrayNode getObjectsWithTypes(JsonNode listNode, JsonNode sourceNode) {
        ArrayNode array = mapper.createArrayNode();
        Iterator<JsonNode> listIterator = listNode.elements();

        while (listIterator.hasNext()) {
            JsonNode type = listIterator.next();
            String typeName = type.textValue();
            JsonNode typeObject = sourceNode.path(typeName);
            ((ObjectNode) typeObject).put("type", typeName);
            array.add(typeObject);
        }

        return array;
    }
}