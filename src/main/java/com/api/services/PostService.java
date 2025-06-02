package com.api.services;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;

import com.api.constants.ApiConstants;
import com.api.constants.HttpMethod;
import com.api.utils.RequestBuilderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    public HttpResponse<String> createPost(String body) {
        HttpRequest request = RequestBuilderUtil.createRequest(
                "posts",
                body,
                HttpMethod.POST,
                ApiConstants.BEARER_TOKEN
        );
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.info("POST request to: {}", request.uri());
            LOGGER.info("Request body: {}", body != null ? body : "null");
            LOGGER.info("Status: {}, Response: {}", response.statusCode(), response.body());
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("POST request to posts failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
