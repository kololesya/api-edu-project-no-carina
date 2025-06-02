package com.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestDataConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = TestDataConfig.class.getClassLoader()
                .getResourceAsStream("testdata.properties")) {
            if (input == null) {
                throw new RuntimeException("testdata.properties not found in resources");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load testdata.properties: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
