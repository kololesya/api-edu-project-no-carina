package com.api.models;

import java.util.HashMap;
import java.util.Map;

public class PostFactory {

    public static Map<String, String> createPostData(int userId) {
        Map<String, String> data = new HashMap<>();
        data.put("title", "Test post title");
        data.put("body", "This is a test post body.");
        data.put("user_id", String.valueOf(userId));
        return data;
    }
}
