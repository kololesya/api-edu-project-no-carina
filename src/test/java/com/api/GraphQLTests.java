package com.api;

import java.util.Map;
import java.net.http.HttpResponse;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.models.UserFactory;

import static com.api.constants.HttpStatus.OK;
import static com.api.models.UserFactory.createDefaultUserDataWithRandomEmail;
import static com.api.models.UserFactory.createUpdatedUserData;
import static com.api.utils.GraphQLResponseValidator.*;

public class GraphQLTests extends BaseGraphQLTest {

    @Test
    public void getAllUsersTest() {
        HttpResponse<String> response = graphQLService.getAllUsers();
        validateGetAllUsersResponse(response);
    }

    @Test
    public void getUserByIdTest() {
        Map<String, String> expectedData = createDefaultUserDataWithRandomEmail();
        int userId = graphQLService.createUserAndGetId(expectedData);
        Map<String, String> actualData = graphQLService.getUserDataById(userId);
        validateUserData(expectedData, actualData);
    }

    @Test
    public void createUserTest() {
        Map<String, String> expectedData = createDefaultUserDataWithRandomEmail();
        int userId = graphQLService.createUserAndGetId(expectedData);
        Map<String, String> actualData = graphQLService.getUserDataById(userId);
        validateUserData(expectedData, actualData);
    }

    @Test
    public void updateUserTest() {
        Map<String, String> originalData = createDefaultUserDataWithRandomEmail();
        int userId = graphQLService.createUserAndGetId(originalData);
        Map<String, String> updatedData = createUpdatedUserData(originalData);
        HttpResponse<String> response = graphQLService.updateUser(userId, updatedData);
        Assert.assertEquals(response.statusCode(), OK.getCode(), "Expected status code 200 after user update");
        Assert.assertFalse(response.body().contains("errors"), "Response contains GraphQL errors");
        Map<String, String> actualData = graphQLService.getUserDataById(userId);
        validateUserData(updatedData, actualData);
    }

    @Test
    public void deleteUserTest() {
        Map<String, String> originalData = createDefaultUserDataWithRandomEmail();
        int userId = graphQLService.createUserAndGetId(originalData);
        HttpResponse<String> deleteResponse = graphQLService.deleteUser(userId);
        Assert.assertEquals(deleteResponse.statusCode(), OK.getCode(), "Expected status 200 after deletion");
        HttpResponse<String> responseAfterDeletion = graphQLService.getUserById(userId);
        Assert.assertTrue(responseAfterDeletion.body().contains("errors"), "Deleted user should not be retrievable");
    }
}
