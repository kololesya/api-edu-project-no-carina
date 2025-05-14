package com.api.models;

import java.util.HashMap;
import java.util.Map;

import com.api.config.TestDataConfig;

public class UserFactory {

    public static Map<String, String> createDefaultUserData() {
        Map<String, String> data = new HashMap<>();
        data.put("name", TestDataConfig.get("user.name"));
        data.put("gender", TestDataConfig.get("user.gender"));
        data.put("email", "olesya" + System.currentTimeMillis() + "@test.com");
        data.put("status", TestDataConfig.get("user.status"));
        return data;
    }
}
