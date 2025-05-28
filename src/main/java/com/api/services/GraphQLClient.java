package com.api.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.api.constants.ApiConstants.BEARER_TOKEN;
import static com.api.constants.ApiConstants.GRAPHQL_ENDPOINT;

public class GraphQLClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLClient.class);

    public HttpResponse<String> sendRequest(String query, Map<String, Object> variables) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("query", query);
            body.put("variables", variables);
            String requestBody = OBJECT_MAPPER.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GRAPHQL_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + BEARER_TOKEN)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.info("POST request to: {}", GRAPHQL_ENDPOINT);
            LOGGER.info("Request body: {}", requestBody);
            LOGGER.info("Status: {}, Response: {}", response.statusCode(), response.body());
            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to send GraphQL request", e);
        }
    }
}
