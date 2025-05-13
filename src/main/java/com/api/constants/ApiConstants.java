package com.api.constants;

import com.api.config.ConfigLoader;

public class ApiConstants {
    public static final String BASE_URL = ConfigLoader.get("base.url");
    public static final String BEARER_TOKEN = ConfigLoader.get("bearer.token");
}

