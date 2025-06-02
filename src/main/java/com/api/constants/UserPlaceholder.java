package com.api.constants;

public enum UserPlaceholder {
    NAME("name"),
    EMAIL("email"),
    GENDER("gender"),
    STATUS("status");

    private final String key;

    UserPlaceholder(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getPlaceholder() {
        return "{{" + key + "}}";
    }
}
