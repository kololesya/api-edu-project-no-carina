package com.api.models;

import com.api.builders.UserBuilder;
import com.api.constants.UserTestData;

import java.util.Map;


import static com.api.constants.UserTestData.UPDATED_NAME;
import static com.api.constants.UserTestData.UPDATED_STATUS;

public class UserFactory {

    public static Map<String, String> createDefaultUserDataWithRandomEmail() {
        return new UserBuilder()
                .withName(UserTestData.VALID_NAME)
                .withGender(UserTestData.VALID_GENDER)
                .withStatus(UserTestData.VALID_STATUS)
                .withRandomEmail()
                .build();
    }

    public static Map<String, String> createInvalidUserData() {
        return new UserBuilder()
                .withName(UserTestData.VALID_NAME)
                .withGender(UserTestData.INVALID_GENDER)
                .withStatus(UserTestData.VALID_STATUS)
                .withEmail(UserTestData.INVALID_EMAIL)
                .build();
    }

    public static Map<String, String> createUpdatedUserData(Map<String, String> original) {
        return new UserBuilder()
                .withName(UPDATED_NAME)
                .withGender(original.get("gender"))
                .withStatus(UPDATED_STATUS)
                .withEmail(original.get("email"))
                .build();
    }
}
