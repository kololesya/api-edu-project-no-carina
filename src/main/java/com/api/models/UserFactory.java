package com.api.models;

import java.util.Map;

import com.api.config.TestDataConfig;

public class UserFactory {

    public static Map<String, String> createDefaultUserData() {
        return Map.of(
                "name", TestDataConfig.get("user.name"),
                "gender", TestDataConfig.get("user.gender"),
                "email", "olesya" + System.currentTimeMillis() + "@test.com",
                "status", TestDataConfig.get("user.status")
        );
    }

    public static Map<String, String> updateUserData() {
        return Map.of(
                "name", TestDataConfig.get("user.updated.name"),
                "email", "updated" + System.currentTimeMillis() + "@example.com",
                "status", TestDataConfig.get("user.updated.status")
        );
    }
}
