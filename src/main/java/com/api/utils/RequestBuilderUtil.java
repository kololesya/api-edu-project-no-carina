package com.api.utils;

import java.net.URI;
import java.net.http.HttpRequest;

import com.api.constants.ApiConstants;
import com.api.constants.HttpMethod;

public class RequestBuilderUtil {

    public static HttpRequest createRequest(String path, String body, HttpMethod method, String token) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(ApiConstants.BASE_URL + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token);

        return switch (method) {
            case GET -> requestBuilder.GET().build();
            case POST -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body)).build();
            case PUT -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body)).build();
            case DELETE -> requestBuilder.DELETE().build();
        };
    }
}
