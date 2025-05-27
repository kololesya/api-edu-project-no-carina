package com.api.helpers;

import java.net.http.HttpResponse;

import com.jayway.jsonpath.JsonPath;

import com.api.services.UserService;
import com.api.utils.RequestBodyBuilderUtil;

public class UserTestHelper {

    public static int createUserAndGetId(UserService userService) {
        String requestBody = RequestBodyBuilderUtil.buildDefaultUserRequestBody();
        HttpResponse<String> response = userService.createUser(requestBody);
        return JsonPath.read(response.body(), "$.id");
    }
}
