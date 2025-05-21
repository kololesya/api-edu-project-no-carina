package com.api;

import java.net.http.HttpResponse;
import java.util.Map;

import com.api.utils.RequestBodyBuilderUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.models.UserFactory;
import com.api.constants.HttpStatus;
import com.jayway.jsonpath.JsonPath;

import static com.api.Utils.ResponseAssertUtils.assertUserFieldsPresent;
import static com.api.constants.TestDataConstants.NON_EXISTENT_USER_ID;

public class UserTests extends BaseApiTest {

    @Test
    public void createValidUserTest() {
        String requestBody = getValidUserRequestBody();
        HttpResponse<String> response = userService.createUser(requestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.CREATED.getCode(), "Expected status 201 when creating a valid user");
    }

    @Test
    public void createUserWithInvalidDataTest() {
        String requestBody = getInvalidUserRequestBody();
        HttpResponse<String> response = userService.createUser(requestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.UNPROCESSABLE_ENTITY.getCode(), "Expected status 422 when sending invalid user data");
    }

    @Test
    public void createUserWithExistingEmailTest() {
        String requestBody = getValidUserRequestBody();
        HttpResponse<String> firstResponse = userService.createUser(requestBody);
        Assert.assertEquals(firstResponse.statusCode(), HttpStatus.CREATED.getCode(), "Expected status 201 when creating first user");
        HttpResponse<String> secondResponse = userService.createUser(requestBody);
        Assert.assertEquals(secondResponse.statusCode(), HttpStatus.UNPROCESSABLE_ENTITY.getCode(), "Expected status 422 for duplicate email");
        Assert.assertTrue(secondResponse.body().contains("email"), "Expected 'email' field error message for duplicate email");
    }

    @Test
    public void updateExistingUserTest() {
        Map<String, String> userData = getValidUserData();
        String requestBody = RequestBodyBuilderUtil.buildUserRequestBody(userData);
        HttpResponse<String> createResponse = userService.createUser(requestBody);
        Assert.assertEquals(createResponse.statusCode(), HttpStatus.CREATED.getCode(), "Expected status 201 when creating a user");
        int userId = JsonPath.read(createResponse.body(), "$.id");
        String updatedRequestBody = getUpdatedUserRequestBody(userData);
        HttpResponse<String> updateResponse = userService.updateUser(userId, updatedRequestBody);
        Assert.assertEquals(updateResponse.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when updating a user");
        String responseBody = updateResponse.body();
        assertUserFieldsPresent(responseBody);
    }

    @Test
    public void getAllUsersTest() {
        HttpResponse<String> response = userService.getAllUsers();
        Assert.assertEquals(response.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when retrieving all users");
        String responseBody = response.body();
        assertUserFieldsPresent(responseBody);
    }

    @Test
    public void deleteUserTest() {
        String requestBody = getValidUserRequestBody();
        HttpResponse<String> createResponse = userService.createUser(requestBody);
        Assert.assertEquals(createResponse.statusCode(), HttpStatus.CREATED.getCode(), "Expected status 201 when creating a user");
        int userId = JsonPath.read(createResponse.body(), "$.id");
        HttpResponse<String> deleteResponse = userService.deleteUser(userId);
        Assert.assertEquals(deleteResponse.statusCode(), HttpStatus.NO_CONTENT.getCode(), "Expected status 204 when deleting a user");
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.NOT_FOUND.getCode(), "Expected status 404 when retrieving deleted user");
    }

    @Test
    public void getUserByIdTest() {
        String requestBody = getValidUserRequestBody();
        HttpResponse<String> createResponse = userService.createUser(requestBody);
        Assert.assertEquals(createResponse.statusCode(), HttpStatus.CREATED.getCode(), "Expected status 201 when creating a user");
        int userId = JsonPath.read(createResponse.body(), "$.id");
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when retrieving a user by ID");
        String responseBody = getResponse.body();
        Assert.assertTrue(responseBody.contains("\"id\":" + userId), "Response does not contain correct 'id'");
        assertUserFieldsPresent(responseBody);
    }

    @Test
    public void getDeletedUserShouldReturn404Test() {
        String requestBody = getValidUserRequestBody();
        HttpResponse<String> createResponse = userService.createUser(requestBody);
        int userId = JsonPath.read(createResponse.body(), "$.id");
        HttpResponse<String> deleteResponse = userService.deleteUser(userId);
        Assert.assertEquals(deleteResponse.statusCode(), HttpStatus.NO_CONTENT.getCode(), "Expected status 204 when deleting user");
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.NOT_FOUND.getCode(), "Expected status 404 when retrieving deleted user");
    }

    @Test
    public void createUserWithEmptyBodyTest() {
        String emptyBody = "{}";
        HttpResponse<String> response = userService.createUser(emptyBody);
        String responseBody = response.body();
        Assert.assertEquals(response.statusCode(), HttpStatus.UNPROCESSABLE_ENTITY.getCode(), "Expected status 422 when creating user with empty body");
        assertUserFieldsPresent(responseBody);
    }

    @Test
    public void getNonExistingUserTest() {
        HttpResponse<String> response = userService.getUserById(NON_EXISTENT_USER_ID);
        Assert.assertEquals(response.statusCode(), HttpStatus.NOT_FOUND.getCode(), "Expected status 404 for non-existing user");
        Assert.assertTrue(response.body().contains("Resource not found"), "Expected 'Resource not found' message in response");
    }

    private String getInvalidUserRequestBody() {
        Map<String, String> invalidData = UserFactory.createInvalidUserData();
        return RequestBodyBuilderUtil.buildUserRequestBody(invalidData);
    }

    private String getValidUserRequestBody() {
        Map<String, String> userData = UserFactory.createDefaultUserData();
        return RequestBodyBuilderUtil.buildUserRequestBody(userData);
    }

    private String getUpdatedUserRequestBody(Map<String, String> originalUserData) {
        originalUserData.put("name", "Updated Name");
        originalUserData.put("status", "inactive");
        return RequestBodyBuilderUtil.buildUserRequestBody(originalUserData);
    }

    private Map<String, String> getValidUserData() {
        return UserFactory.createDefaultUserData();
    }
}
