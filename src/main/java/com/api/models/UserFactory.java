package com.api.models;

import com.api.builders.UserBuilder;
import com.api.constants.UserTestData;

import java.util.Map;

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

    public static Map<String, String> createUpdatedUserData(Map<String, String> original, String updatedName, String updatedStatus) {
        return new UserBuilder()
                .withName(updatedName)
                .withGender(original.get("gender"))
                .withStatus(updatedStatus)
                .withEmail(original.get("email"))
                .build();
    }
}
