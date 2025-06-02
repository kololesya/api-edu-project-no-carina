package com.api.builders;

import com.api.constants.UserPlaceholder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserBuilder {

    private final Map<String, String> data = new HashMap<>();

    public UserBuilder withName(String name) {
        data.put(UserPlaceholder.NAME.getKey(), name);
        return this;
    }

    public UserBuilder withGender(String gender) {
        data.put(UserPlaceholder.GENDER.getKey(), gender);
        return this;
    }

    public UserBuilder withStatus(String status) {
        data.put(UserPlaceholder.STATUS.getKey(), status);
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

    public Map<String, String> build() {
        return data;
    }
}
