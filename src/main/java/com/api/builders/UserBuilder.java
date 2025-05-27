package com.api.builders;

import com.api.constants.UserPlaceholder;
import com.api.config.TestDataConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.api.constants.InvalidUserData.INVALID_EMAIL;
import static com.api.constants.InvalidUserData.INVALID_GENDER;

public class UserBuilder {

    private final Map<String, String> data = new HashMap<>();

    public UserBuilder withDefaultValues() {
        data.put(UserPlaceholder.NAME.getKey(), TestDataConfig.get("user.name"));
        data.put(UserPlaceholder.GENDER.getKey(), TestDataConfig.get("user.gender"));
        data.put(UserPlaceholder.STATUS.getKey(), TestDataConfig.get("user.status"));
        return this;
    }

    public UserBuilder withEmail(String email) {
        data.put(UserPlaceholder.EMAIL.getKey(), email);
        return this;
    }

    public UserBuilder withRandomEmail() {
        String email = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        return withEmail(email);
    }

    public UserBuilder withInvalidEmail() {
        return withEmail(INVALID_EMAIL.getValue ());
    }

    public UserBuilder withGender(String gender) {
        data.put(UserPlaceholder.GENDER.getKey(), gender);
        return this;
    }

    public UserBuilder withInvalidGender() {
        return withGender(INVALID_GENDER.getValue());
    }

    public UserBuilder withName(String name) {
        data.put(UserPlaceholder.NAME.getKey(), name);
        return this;
    }

    public UserBuilder withStatus(String status) {
        data.put(UserPlaceholder.STATUS.getKey(), status);
        return this;
    }

    public Map<String, String> build() {
        return data;
    }
}
