package com.api.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.Map;

import static com.api.constants.ApiConstants.*;

public class APIService {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static HttpResponse<String> sendRequest(String path, String body, String method, Map<String, String> headers) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .header("Authorization", "Bearer " + BEARER_TOKEN)
                    .header("Content-Type", "application/json");

            headers.forEach(builder::header);

            HttpRequest request = switch (method.toUpperCase()) {
                case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body)).build();
                case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body)).build();
                case "DELETE" -> builder.DELETE().build();
                case "GET" -> builder.GET().build();
                default -> throw new IllegalArgumentException("Unsupported method: " + method);
            };

            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("HTTP request failed: " + e.getMessage(), e);
        }
    }
}

