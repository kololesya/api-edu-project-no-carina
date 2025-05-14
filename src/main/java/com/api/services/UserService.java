package com.api.services;

import com.api.constants.ApiConstants;
import com.api.constants.HttpMethod;
import com.api.constants.JsonConstant;
import com.api.models.UserFactory;
import com.api.utils.LoggingUtils;
import com.api.utils.RequestBuilderUtil;
import com.api.utils.TemplateUtils;
import com.jayway.jsonpath.JsonPath;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public HttpResponse<String> createUser(String body) {
        HttpRequest request = RequestBuilderUtil.createRequest(
                "users",
                body,
                HttpMethod.POST,
                ApiConstants.BEARER_TOKEN
        );
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            LoggingUtils.logRequestAndResponse(LOGGER, "POST", request.uri().toString(), body, response);
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Request failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int createUserAndReturnId() {
        Map<String, String> userData = UserFactory.createDefaultUserData();
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        String requestBody = TemplateUtils.populateTemplate(template, userData);
        HttpResponse<String> response = createUser(requestBody);
        return JsonPath.read(response.body(), "$.id");
    }

    public HttpResponse<String> getUserById(int userId) {
        HttpRequest request = RequestBuilderUtil.createRequest(
                "users/" + userId,
                "",
                HttpMethod.GET,
                ApiConstants.BEARER_TOKEN
        );
        return send(request);
    }

    public HttpResponse<String> getAllUsers() {
        HttpRequest request = RequestBuilderUtil.createRequest(
                "users",
                null,
                HttpMethod.GET,
                ApiConstants.BEARER_TOKEN
        );
        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            LoggingUtils.logRequestAndResponse(
                    LOGGER,
                    "GET",
                    request.uri().toString(),
                    null,
                    response
            );
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("GET /users failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> updateUser(int userId, String body) {
        HttpRequest request = RequestBuilderUtil.createRequest(
                "users/" + userId,
                body,
                HttpMethod.PUT,
                ApiConstants.BEARER_TOKEN
        );

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            LoggingUtils.logRequestAndResponse(LOGGER, "PUT", ApiConstants.BASE_URL + "users/" + userId, body, response);
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Request failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> deleteUser(int userId) {
        HttpRequest request = RequestBuilderUtil.createRequest(
                "users/" + userId,
                null,
                HttpMethod.DELETE,
                ApiConstants.BEARER_TOKEN
        );
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            LoggingUtils.logRequestAndResponse(LOGGER, "DELETE", ApiConstants.BASE_URL + "users/" + userId, null, response);
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Request failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Request failed", e);
        }
    }
}
