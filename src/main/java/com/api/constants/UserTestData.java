package com.api.constants;

import com.api.config.TestDataConfig;

public class UserTestData {

    public static final String VALID_NAME = TestDataConfig.get("user.name");
    public static final String VALID_GENDER = TestDataConfig.get("user.gender");
    public static final String VALID_STATUS = TestDataConfig.get("user.status");
    public static final String UPDATED_NAME = TestDataConfig.get("user.updated.name");
    public static final String UPDATED_STATUS = TestDataConfig.get("user.updated.status");

    public static final String INVALID_EMAIL = "invalid-email-format";
    public static final String INVALID_GENDER = "alien";
}
