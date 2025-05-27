package com.api.constants;

public enum InvalidUserData {

    INVALID_EMAIL("invalid-email-format"),
    INVALID_GENDER("alien"),
    INVALID_STATUS("ghost"),
    EMPTY_NAME(""),
    NULL_NAME(null);

    private final String value;

    InvalidUserData(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
