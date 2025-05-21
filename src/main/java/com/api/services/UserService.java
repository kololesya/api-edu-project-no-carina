package com.api.services;

import com.api.constants.ApiConstants;
import com.api.constants.HttpMethod;
import com.api.utils.LoggingUtils;
import com.api.utils.RequestBodyBuilderUtil;
import com.api.utils.RequestBuilderUtil;
import com.jayway.jsonpath.JsonPath;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public HttpResponse<String> createUser(String body) {
        return sendRequest("users", body, HttpMethod.POST);
    }

    public int createUserAndReturnId() {
        String requestBody = RequestBodyBuilderUtil.buildDefaultUserRequestBody();
        HttpResponse<String> response = createUser(requestBody);
        return JsonPath.read(response.body(), "$.id");
    }

    public HttpResponse<String> getUserById(int userId) {
        return sendRequest("users/" + userId, "", HttpMethod.GET);
    }

    public HttpResponse<String> getAllUsers() {
        return sendRequest("users", null, HttpMethod.GET);
    }

    public HttpResponse<String> updateUser(int userId, String body) {
        return sendRequest("users/" + userId, body, HttpMethod.PUT);
    }

    public HttpResponse<String> deleteUser(int userId) {
        return sendRequest("users/" + userId, null, HttpMethod.DELETE);
    }

    private HttpResponse<String> sendRequest(String path, String body, HttpMethod method) {
        HttpRequest request = RequestBuilderUtil.createRequest(path, body, method, ApiConstants.BEARER_TOKEN);
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            LoggingUtils.logRequestAndResponse(LOGGER, method.name(), request.uri().toString(), body, response);
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("{} request to {} failed: {}", method.name(), path, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
