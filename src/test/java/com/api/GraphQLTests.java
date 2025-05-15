package com.api;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.constants.HttpStatus;
import com.api.services.GraphQLService;

public class GraphQLTests {

    @Test
    public void getAllUsersGraphQLTest() {
        GraphQLService graphQLService = new GraphQLService();
        HttpResponse<String> response = graphQLService.getAllUsersQuery();
        Assert.assertEquals(
                response.statusCode(),
                HttpStatus.OK.getCode(),
                "Expected status code 200 for successful GraphQL query"
        );
        String responseBody = response.body();
        Assert.assertTrue(responseBody.contains("nodes"), "Response should contain 'nodes'");
        Assert.assertTrue(responseBody.contains("id"), "Response should contain 'id'");
        Assert.assertTrue(responseBody.contains("name"), "Response should contain 'name'");
        Assert.assertTrue(responseBody.contains("email"), "Response should contain 'email'");
        Assert.assertTrue(responseBody.contains("gender"), "Response should contain 'gender'");
        Assert.assertTrue(responseBody.contains("status"), "Response should contain 'status'");
    }

    @Test
    public void getUserByIdGraphQLTest() {
        GraphQLService graphQLService = new GraphQLService();
        int userId = graphQLService.createUserAndReturnIdViaGraphQL();
        HttpResponse<String> response = graphQLService.getUserByIdQuery(userId);
        Assert.assertEquals(response.statusCode(), 200, "Expected status 200");
        String responseBody = response.body();
        Assert.assertTrue(responseBody.contains("\"id\":" + userId), "Response should contain correct user ID");
        Assert.assertTrue(responseBody.contains("name"), "Response should contain 'name'");
        Assert.assertTrue(responseBody.contains("email"), "Response should contain 'email'");
        Assert.assertTrue(responseBody.contains("gender"), "Response should contain 'gender'");
        Assert.assertTrue(responseBody.contains("status"), "Response should contain 'status'");
    }

    @Test
    public void createUserGraphQLTest() {
        GraphQLService graphQLService = new GraphQLService();
        int userId = graphQLService.createUserAndReturnIdViaGraphQL();
        Assert.assertTrue(userId > 0, "User ID should be a positive number");
    }

    @Test
    public void updateUserGraphQLTest() {
        GraphQLService graphQLService = new GraphQLService();
        int userId = graphQLService.createUserAndReturnIdViaGraphQL();
        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("name", "Updated Name");
        updatedData.put("gender", "male");
        updatedData.put("email", "updated" + System.currentTimeMillis() + "@test.com");
        updatedData.put("status", "inactive");
        HttpResponse<String> response = graphQLService.updateUserViaGraphQL(userId, updatedData);
        Assert.assertEquals(response.statusCode(), 200, "Expected status 200");
        String responseBody = response.body();
        Assert.assertTrue(responseBody.contains("Updated Name"), "Response should contain updated name");
        Assert.assertTrue(responseBody.contains("inactive"), "Response should contain updated status");
    }

    @Test
    public void deleteUserGraphQLTest() {
        GraphQLService graphQLService = new GraphQLService();
        int userId = graphQLService.createUserAndReturnIdViaGraphQL();
        HttpResponse<String> response = graphQLService.deleteUserViaGraphQL(userId);
        Assert.assertEquals(response.statusCode(), 200, "Expected status 200");
        Assert.assertTrue(response.body().contains("\"id\":" + userId), "Response should contain deleted user's ID");
    }
}
