package com.api;

import java.net.http.HttpResponse;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.api.constants.HttpStatus;

import static com.api.Utils.ResponseAssertUtils.assertUserFieldsPresent;
import static com.api.constants.TestDataConstants.NON_EXISTENT_USER_ID;
import static com.api.helpers.UserTestHelper.createUserAndGetId;
import static com.api.models.UserFactory.createDefaultUserData;
import static com.api.utils.RequestBodyBuilderUtil.*;

public class UserTests extends BaseApiTest {

    @DataProvider(name = "userCreationData")
    public static Object[][] userCreationData() {
        return new Object[][]{
                {"Valid user", buildDefaultUserRequestBody(), HttpStatus.CREATED.getCode()},
                {"Invalid user", buildInvalidUserRequestBody(), HttpStatus.UNPROCESSABLE_ENTITY.getCode()},
                {"Duplicate email", buildDefaultUserRequestBody(), HttpStatus.UNPROCESSABLE_ENTITY.getCode()},
                {"Empty body", "{}", HttpStatus.UNPROCESSABLE_ENTITY.getCode()}
        };
    }

    @Test(dataProvider = "userCreationData")
    public void createUserTest(String description, String requestBody, int expectedStatusCode) {
        if ("Duplicate email".equals(description)) {
            userService.createUser(requestBody);
        }
        HttpResponse<String> response = userService.createUser(requestBody);
        Assert.assertEquals(response.statusCode(), expectedStatusCode,
                "Expected status " + expectedStatusCode + " for case: " + description);
        if (expectedStatusCode == HttpStatus.UNPROCESSABLE_ENTITY.getCode()) {
            Assert.assertTrue(response.body().contains("email") || response.body().contains("name") || response.body().contains("gender"),
                    "Expected validation errors in response for: " + description);
        }
        if (expectedStatusCode == HttpStatus.OK.getCode() || expectedStatusCode == HttpStatus.CREATED.getCode()) {
            assertUserFieldsPresent(response.body());
        }
    }

    @Test
    public void updateExistingUserTest() {
        Map<String, String> originalUserData = createDefaultUserData();
        int userId = createUserAndGetId(userService);
        String updatedRequestBody = buildUpdatedUserRequestBody(originalUserData, "Updated Name", "inactive");
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
        int userId = createUserAndGetId(userService);
        HttpResponse<String> deleteResponse = userService.deleteUser(userId);
        Assert.assertEquals(deleteResponse.statusCode(), HttpStatus.NO_CONTENT.getCode(), "Expected status 204 when deleting a user");
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.NOT_FOUND.getCode(), "Expected status 404 when retrieving deleted user");
    }

    @Test
    public void getUserByIdTest() {
        int userId = createUserAndGetId(userService);
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when retrieving a user by ID");
        String responseBody = getResponse.body();
        Assert.assertTrue(responseBody.contains("\"id\":" + userId), "Response does not contain correct 'id'");
        assertUserFieldsPresent(responseBody);
    }

    @Test
    public void deleteNonExistUserTest () {
        int userId = createUserAndGetId(userService);
        HttpResponse<String> deleteResponse = userService.deleteUser(userId);
        Assert.assertEquals(deleteResponse.statusCode(), HttpStatus.NO_CONTENT.getCode(), "Expected status 204 when deleting user");
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.NOT_FOUND.getCode(), "Expected status 404 when retrieving deleted user");
    }

    @Test
    public void getNonExistingUserTest() {
        HttpResponse<String> response = userService.getUserById(NON_EXISTENT_USER_ID);
        Assert.assertEquals(response.statusCode(), HttpStatus.NOT_FOUND.getCode(), "Expected status 404 for non-existing user");
        Assert.assertTrue(response.body().contains("Resource not found"), "Expected 'Resource not found' message in response");
    }
}
