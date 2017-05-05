package com.qreal.wmp.selector;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Map;

@Component
@PropertySource("classpath:application.properties")
public class ConfigsMerger {
    
    private final JsonParser parser = new JsonParser();
    
    @Value("${selectorConfig}")
    private String configFile;
    
    /** Generates one config file from all which are used. */
    public JsonObject generateCommonConfig() throws FileNotFoundException {
        String path = getClass().getClassLoader().getResource(configFile).getPath();
        JsonElement jsonElement = parser.parse(new FileReader(path));
        JsonObject initialConfig = jsonElement.getAsJsonObject();
        return merge(initialConfig, path);
    }
    
    private JsonObject merge(JsonObject parent, String path) throws FileNotFoundException {
        JsonObject result = new JsonObject();
        for (Map.Entry<String, JsonElement> childEntry: parent.entrySet()) {
            if (childEntry.getValue().isJsonPrimitive()) {
                String pathLink = "$path:";
                if (childEntry.getValue().getAsJsonPrimitive().isString()
                        && childEntry.getValue().getAsString().startsWith(pathLink)) {
                    
                    String stringValue = childEntry.getValue().getAsString();
                    String newPath = getFolderPath(path) + "/"
                            + stringValue.substring(pathLink.length(), stringValue.length());
                    
                    JsonObject parsedChild;
                    parsedChild = parser.parse(new FileReader(newPath)).getAsJsonObject();
                    result.add(childEntry.getKey(), merge(parsedChild, newPath));
                } else {
                    result.add(childEntry.getKey(), childEntry.getValue());
                }
            } else {
                result.add(childEntry.getKey(), merge(childEntry.getValue().getAsJsonObject(), path));
            }
        }
        return result;
    }
    
    private String getFolderPath(String path) {
        String[] tmpPath = path.split("/");
        return String.join("/", Arrays.copyOf(tmpPath, tmpPath.length - 1));
    }
}
