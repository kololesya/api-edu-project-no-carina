package com.api.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.api.constants.JsonConstant;
import com.api.models.UserFactory;
import com.api.utils.TemplateUtils;
import com.jayway.jsonpath.JsonPath;
import com.api.config.ConfigLoader;
import com.api.utils.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphQLService {

    private static final String GRAPHQL_ENDPOINT = ConfigLoader.get("graphql.endpoint");
    private static final String BEARER_TOKEN = ConfigLoader.get("bearer.token");
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLService.class);

    public HttpResponse<String> sendGraphQLRequest(String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GRAPHQL_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            LoggingUtils.logRequestAndResponse(LOGGER, "POST", request.uri().toString(), body, response);
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("GraphQL request failed: {}", e.getMessage());
            throw new RuntimeException("GraphQL request failed", e);
        }
    }

    public HttpResponse<String> getAllUsersQuery() {
        String query = "{ users { nodes { id name email gender status } } }";
        String requestBody = String.format("{\"query\": \"%s\"}", query);
        return sendGraphQLRequest(requestBody);
    }

    public HttpResponse<String> getUserByIdQuery(int userId) {
        String query = String.format("query { user(id: %d) { id name email gender status } }", userId);
        String requestBody = String.format("{\"query\": \"%s\"}", query);
        return sendGraphQLRequest(requestBody);
    }

    public int createUserAndReturnIdViaGraphQL() {
        Map<String, String> userData = UserFactory.createDefaultUserData();
        String mutationTemplate = TemplateUtils.loadTemplate(JsonConstant.GRAPHQL_USER_MUTATION_TEMPLATE_PATH);
        Map<String, Object> inputMap = new HashMap<>(userData);
        Map<String, Object> variables = Map.of("input", inputMap);
        HttpResponse<String> response = sendGraphQLRequestWithVariables(mutationTemplate, variables);
        if (response.statusCode() != 200 || response.body().contains("errors")) {
            throw new RuntimeException("Failed to create user via GraphQL: " + response.body());
        }
        return JsonPath.read(response.body(), "$.data.createUser.user.id");
    }

    public HttpResponse<String> updateUserViaGraphQL(int id, Map<String, String> newUserData) {
        String mutationTemplate = TemplateUtils.loadTemplate(JsonConstant.GRAPHQL_USER_UPDATE_TEMPLATE_PATH);
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("id", id);
        inputMap.putAll(newUserData);
        Map<String, Object> variables = Map.of("input", inputMap);
        return sendGraphQLRequestWithVariables(mutationTemplate, variables);
    }

    private HttpResponse<String> sendGraphQLRequestWithVariables(String query, Map<String, Object> variables) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("query", query);
        requestMap.put("variables", variables);
        String requestBody;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            requestBody = objectMapper.writeValueAsString(requestMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }
        HttpResponse<String> response = sendGraphQLRequest(requestBody);
        LoggingUtils.logRequestAndResponse(LOGGER, "POST", GRAPHQL_ENDPOINT, requestBody, response);
        return response;
    }

    public HttpResponse<String> deleteUserViaGraphQL(int id) {
        try {
            String mutation = TemplateUtils.loadTemplate(JsonConstant.GRAPHQL_USER_DELETE_TEMPLATE_PATH).trim();
            Map<String, Object> variables = Map.of(
                    "input", Map.of("id", id)
            );
            Map<String, Object> requestMap = Map.of(
                    "query", mutation,
                    "variables", variables
            );
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(requestMap);
            HttpResponse<String> response = sendGraphQLRequest(requestBody);
            LoggingUtils.logRequestAndResponse(LOGGER, "POST", GRAPHQL_ENDPOINT, requestBody, response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to send GraphQL deleteUser mutation", e);
        }
    }
}
