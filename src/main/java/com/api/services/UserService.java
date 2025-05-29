package com.api.services;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.util.Map;

import com.api.constants.ApiConstants;
import com.api.constants.HttpMethod;
import com.api.utils.RequestBodyBuilderUtil;
import com.api.utils.RequestBuilderUtil;
import com.jayway.jsonpath.JsonPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.api.constants.JsonPathConstants.USER_ID;

public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public HttpResponse<String> createUser(String body) {
        return sendRequest("users", body, HttpMethod.POST);
    }

    public int createUserAndReturnId(Map<String, String> userData) {
        String requestBody = RequestBodyBuilderUtil.buildUserRequestBody(userData);
        HttpResponse<String> response = createUser(requestBody);
        return JsonPath.read(response.body(), USER_ID);
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
            LOGGER.info("{} request to: {}", method.name(), request.uri());
            LOGGER.info("Request body: {}", body != null ? body : "null");
            LOGGER.info("Status: {}, Response: {}", response.statusCode(), response.body());
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("{} request to {} failed: {}", method.name(), path, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
