package com.api.models;

import com.api.builders.UserBuilder;

import java.util.Map;

public class UserFactory {

    public static Map<String, String> createDefaultUserData() {
        return new UserBuilder()
                .withDefaultValues()
                .withRandomEmail()
                .build();
    }

    public static Map<String, String> createInvalidUserData() {
        return new UserBuilder()
                .withDefaultValues()
                .withInvalidEmail()
                .withInvalidGender()
                .build();
    }
}
