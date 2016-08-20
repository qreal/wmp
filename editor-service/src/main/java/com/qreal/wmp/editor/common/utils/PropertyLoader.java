package com.qreal.wmp.editor.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/** Util class for property load.*/
public class PropertyLoader {

    private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    /** Loads property from property file located on a classpath.*/
    public static String load(String fileName, String propertyName) {
        String result = "";
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            logger.error("Property file {} not found", fileName);
            return result;
        }
        try {
            java.util.Properties properties = new java.util.Properties();
            properties.load(input);
            result = properties.getProperty(propertyName);
        } catch (IOException e) {
            logger.error("Exception occurred during processing of {} properties file seeking for property {}",
                    fileName, propertyName, e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                logger.error("Exception occurred during processing of {} properties file seeking for property {}",
                        fileName, propertyName, e);
            }
        }
        return result;

    }
}
