package com.api;

import java.net.http.HttpResponse;
import java.util.Map;

import com.api.constants.JsonConstant;
import com.api.services.UserService;
import com.api.utils.TemplateUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.models.UserFactory;
import com.api.constants.HttpStatus;
import com.jayway.jsonpath.JsonPath;

public class UserTests {

    @Test
    public void createValidUserTest() {
        Map<String, String> userData = UserFactory.createDefaultUserData();
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        String requestBody = TemplateUtils.populateTemplate(template, userData);
        UserService userService = new UserService();
        HttpResponse<String> response = userService.createUser(requestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.CREATED.getCode(), "Expected status 201 when creating a valid user");
    }

    @Test
    public void getAllUsersTest() {
        UserService userService = new UserService();
        HttpResponse<String> response = userService.getAllUsers();
        Assert.assertEquals(response.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when retrieving all users");
        String responseBody = response.body();
        Assert.assertTrue(responseBody.contains("name"), "Response does not contain 'name'");
        Assert.assertTrue(responseBody.contains("email"), "Response does not contain 'email'");
        Assert.assertTrue(responseBody.contains("gender"), "Response does not contain 'gender'");
        Assert.assertTrue(responseBody.contains("status"), "Response does not contain 'status'");
    }

    @Test
    public void updateExistingUserTest() {
        Map<String, String> userData = UserFactory.createDefaultUserData();
        String userTemplate = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        String userBody = TemplateUtils.populateTemplate(userTemplate, userData);
        UserService userService = new UserService();
        HttpResponse<String> createResponse = userService.createUser(userBody);
        Assert.assertEquals(createResponse.statusCode(), HttpStatus.CREATED.getCode(), "Expected status 201 when creating a user");
        int userId = JsonPath.read(createResponse.body(), "$.id");
        userData.put("name", "Updated Name");
        userData.put("status", "inactive");
        String updatedUserBody = TemplateUtils.populateTemplate(userTemplate, userData);
        HttpResponse<String> updateResponse = userService.updateUser(userId, updatedUserBody);
        Assert.assertEquals(updateResponse.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when updating a user");
        String responseBody = updateResponse.body();
        Assert.assertTrue(responseBody.contains("name"), "Response does not contain 'name'");
        Assert.assertTrue(responseBody.contains("email"), "Response does not contain 'email'");
        Assert.assertTrue(responseBody.contains("gender"), "Response does not contain 'gender'");
        Assert.assertTrue(responseBody.contains("status"), "Response does not contain 'status'");
    }

    @Test
    public void deleteUserTest() {
        Map<String, String> userData = UserFactory.createDefaultUserData();
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        String requestBody = TemplateUtils.populateTemplate(template, userData);
        UserService userService = new UserService();
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
        Map<String, String> userData = UserFactory.createDefaultUserData();
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        String requestBody = TemplateUtils.populateTemplate(template, userData);
        UserService userService = new UserService();
        HttpResponse<String> createResponse = userService.createUser(requestBody);
        Assert.assertEquals(createResponse.statusCode(), HttpStatus.CREATED.getCode(), "Expected status 201 when creating a user");
        int userId = JsonPath.read(createResponse.body(), "$.id");
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when retrieving a user by ID");
        String responseBody = getResponse.body();
        Assert.assertTrue(responseBody.contains("\"id\":" + userId), "Response does not contain correct 'id'");
        Assert.assertTrue(responseBody.contains("\"name\":"), "Response does not contain 'name'");
        Assert.assertTrue(responseBody.contains("\"email\":"), "Response does not contain 'email'");
        Assert.assertTrue(responseBody.contains("\"gender\":"), "Response does not contain 'gender'");
        Assert.assertTrue(responseBody.contains("\"status\":"), "Response does not contain 'status'");
    }

    @Test
    public void getDeletedUserShouldReturn404Test() {
        Map<String, String> userData = UserFactory.createDefaultUserData();
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        String requestBody = TemplateUtils.populateTemplate(template, userData);
        UserService userService = new UserService();
        HttpResponse<String> createResponse = userService.createUser(requestBody);
        int userId = JsonPath.read(createResponse.body(), "$.id");
        HttpResponse<String> deleteResponse = userService.deleteUser(userId);
        Assert.assertEquals(deleteResponse.statusCode(), HttpStatus.NO_CONTENT.getCode(), "Expected status 204 when deleting user");
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.NOT_FOUND.getCode(), "Expected status 404 when retrieving deleted user");
    }

    @Test
    public void createUserWithInvalidDataTest() {
        Map<String, String> invalidUserData = Map.of(
                "name", "Invalid User",
                "gender", "invalid_gender",
                "email", "invalid-email-format",
                "status", "not_a_status"
        );
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        String requestBody = TemplateUtils.populateTemplate(template, invalidUserData);
        UserService userService = new UserService();
        HttpResponse<String> response = userService.createUser(requestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.UNPROCESSABLE_ENTITY.getCode(), "Expected status 422 when sending invalid user data");
    }

    @Test
    public void createUserWithExistingEmailTest() {
        Map<String, String> userData = UserFactory.createDefaultUserData();
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        String requestBody = TemplateUtils.populateTemplate(template, userData);
        UserService userService = new UserService();
        HttpResponse<String> firstResponse = userService.createUser(requestBody);
        Assert.assertEquals(firstResponse.statusCode(), HttpStatus.CREATED.getCode(), "Expected status 201 when creating first user");
        HttpResponse<String> secondResponse = userService.createUser(requestBody);
        Assert.assertEquals(secondResponse.statusCode(), HttpStatus.UNPROCESSABLE_ENTITY.getCode(), "Expected status 422 for duplicate email");
        Assert.assertTrue(secondResponse.body().contains("email"), "Expected 'email' field error message for duplicate email");
    }

    @Test
    public void createUserWithEmptyBodyTest() {
        String emptyBody = "{}";
        UserService userService = new UserService();
        HttpResponse<String> response = userService.createUser(emptyBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.UNPROCESSABLE_ENTITY.getCode(), "Expected status 422 when creating user with empty body");
        Assert.assertTrue(response.body().contains("name"), "Expected validation error for missing 'name'");
        Assert.assertTrue(response.body().contains("email"), "Expected validation error for missing 'email'");
        Assert.assertTrue(response.body().contains("gender"), "Expected validation error for missing 'gender'");
        Assert.assertTrue(response.body().contains("status"), "Expected validation error for missing 'status'");
    }

    @Test
    public void getNonExistingUserTest() {
        int nonExistentUserId = 99999999;
        UserService userService = new UserService();
        HttpResponse<String> response = userService.getUserById(nonExistentUserId);
        Assert.assertEquals(response.statusCode(), HttpStatus.NOT_FOUND.getCode(), "Expected status 404 for non-existing user");
        Assert.assertTrue(response.body().contains("Resource not found"), "Expected 'Resource not found' message in response");
    }
}
