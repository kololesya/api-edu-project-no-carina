package com.api.services;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.api.constants.JsonConstant;
import com.api.utils.TemplateUtils;
import com.jayway.jsonpath.JsonPath;

import static com.api.constants.GraphQLConstant.*;

public class GraphQLService {

    private final GraphQLClient graphQLClient = new GraphQLClient();

    public HttpResponse<String> getAllUsers() {
        String query = TemplateUtils.loadTemplate(JsonConstant.GRAPHQL_GET_ALL_USERS_TEMPLATE_PATH);
        return graphQLClient.sendRequest(query, Map.of());
    }

    public HttpResponse<String> getUserById(int userId) {
        String queryTemplate = TemplateUtils.loadTemplate(JsonConstant.GRAPHQL_GET_USER_BY_ID_TEMPLATE_PATH);
        Map<String, Object> variables = Map.of("id", userId);
        return graphQLClient.sendRequest(queryTemplate, variables);
    }

    public Map<String, String> getUserDataById(int userId) {
        HttpResponse<String> response = getUserById(userId);
        return Map.of(
                "name", JsonPath.read(response.body(), USER_NAME),
                "email", JsonPath.read(response.body(), USER_EMAIL),
                "gender", JsonPath.read(response.body(), USER_GENDER),
                "status", JsonPath.read(response.body(), USER_STATUS)
        );
    }

    public HttpResponse<String> updateUser (int id, Map<String, String> newUserData) {
        String mutationTemplate = TemplateUtils.loadTemplate(JsonConstant.GRAPHQL_USER_UPDATE_TEMPLATE_PATH);
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("id", id);
        inputMap.putAll(newUserData);
        Map<String, Object> variables = Map.of("input", inputMap);
        return graphQLClient.sendRequest(mutationTemplate, variables);
    }

    public int createUserAndGetId (Map<String, String> userData) {
        String mutationTemplate = TemplateUtils.loadTemplate(JsonConstant.GRAPHQL_USER_MUTATION_TEMPLATE_PATH);
        Map<String, Object> inputMap = new HashMap<>(userData);
        Map<String, Object> variables = Map.of("input", inputMap);
        HttpResponse<String> response = graphQLClient.sendRequest(mutationTemplate, variables);
        return JsonPath.read(response.body(), CREATED_USER_ID);
    }

    public HttpResponse<String> deleteUser(int id) {
        String mutation = TemplateUtils.loadTemplate(JsonConstant.GRAPHQL_USER_DELETE_TEMPLATE_PATH);
        Map<String, Object> variables = Map.of(
                "input", Map.of("id", id)
        );
        return graphQLClient.sendRequest(mutation, variables);
    }

    public int extractDeletedUserId(HttpResponse<String> response) {
        return JsonPath.read(response.body(), DELETED_USER_ID);
    }
}
