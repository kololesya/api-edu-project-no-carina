package com.api;

import java.net.http.HttpResponse;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.api.constants.HttpStatus;

import static com.api.Utils.ResponseAssertUtils.assertAllUsersContainRequiredFields;
import static com.api.Utils.ResponseAssertUtils.assertUserDataMatches;
import static com.api.constants.TestDataConstants.NON_EXISTENT_USER_ID;
import static com.api.models.UserFactory.createDefaultUserDataWithRandomEmail;
import static com.api.models.UserFactory.createInvalidUserData;
import static com.api.utils.RequestBodyBuilderUtil.buildUserRequestBody;
import static com.api.utils.RequestBodyBuilderUtil.buildUpdatedUserRequestBody;

public class UserTests extends BaseApiTest {

    @DataProvider(name = "userCreationData")
    public static Object[][] userCreationData() {
        return new Object[][]{
                {"Valid user", createDefaultUserDataWithRandomEmail(), HttpStatus.CREATED.getCode()},
                {"Invalid user", createInvalidUserData(), HttpStatus.UNPROCESSABLE_ENTITY.getCode()},
                {"Empty body", null, HttpStatus.UNPROCESSABLE_ENTITY.getCode()}
        };
    }

    @Test(dataProvider = "userCreationData")
    public void createUserTest(String description, Map<String, String> userData, int expectedStatusCode) {
        String requestBody = userData != null ? buildUserRequestBody(userData) : "{}";
        HttpResponse<String> response = userService.createUser(requestBody);
        Assert.assertEquals(response.statusCode(), expectedStatusCode,
                "Expected status " + expectedStatusCode + " for case: " + description);
        if (expectedStatusCode == HttpStatus.UNPROCESSABLE_ENTITY.getCode()) {
            Assert.assertTrue(response.body().contains("email") || response.body().contains("name") || response.body().contains("gender"),
                    "Expected validation errors in response for: " + description);
        }
        if (expectedStatusCode == HttpStatus.CREATED.getCode()) {
            assertUserDataMatches(response.body(), userData);
        }
    }

    @Test
    public void duplicateEmailUserTest() {
        String body = buildUserRequestBody(createDefaultUserDataWithRandomEmail());
        HttpResponse<String> response1 = userService.createUser(body);
        Assert.assertEquals(response1.statusCode(), HttpStatus.CREATED.getCode(), "First user creation failed");
        HttpResponse<String> response2 = userService.createUser(body);
        Assert.assertEquals(response2.statusCode(), HttpStatus.UNPROCESSABLE_ENTITY.getCode(), "Duplicate email should not be allowed");
    }

    @Test
    public void updateExistingUserTest() {
        Map<String, String> originalUserData = createDefaultUserDataWithRandomEmail();
        int userId = userService.createUserAndReturnId(originalUserData);
        String updatedRequestBody = buildUpdatedUserRequestBody(originalUserData, "Updated Name", "inactive");
        HttpResponse<String> updateResponse = userService.updateUser(userId, updatedRequestBody);
        Assert.assertEquals(updateResponse.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when updating a user");
        String responseBody = updateResponse.body();
        assertUserDataMatches(responseBody, originalUserData);
    }

    @Test
    public void getAllUsersTest() {
        HttpResponse<String> response = userService.getAllUsers();
        Assert.assertEquals(response.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when retrieving all users");
        String responseBody = response.body();
        assertAllUsersContainRequiredFields(responseBody);
    }

    @Test
    public void deleteUserTest() {
        Map<String, String> originalUserData = createDefaultUserDataWithRandomEmail();
        int userId = userService.createUserAndReturnId(originalUserData);
        HttpResponse<String> deleteResponse = userService.deleteUser(userId);
        Assert.assertEquals(deleteResponse.statusCode(), HttpStatus.NO_CONTENT.getCode(), "Expected status 204 when deleting a user");
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.NOT_FOUND.getCode(), "Expected status 404 when retrieving deleted user");
    }

    @Test
    public void getUserByIdTest() {
        Map<String, String> originalUserData = createDefaultUserDataWithRandomEmail();
        int userId = userService.createUserAndReturnId(originalUserData);
        HttpResponse<String> getResponse = userService.getUserById(userId);
        Assert.assertEquals(getResponse.statusCode(), HttpStatus.OK.getCode(), "Expected status 200 when retrieving a user by ID");
        String responseBody = getResponse.body();
        assertUserDataMatches(responseBody, originalUserData);
    }

    @Test
    public void deleteNonExistUserTest() {
        Map<String, String> originalUserData = createDefaultUserDataWithRandomEmail();
        int userId = userService.createUserAndReturnId(originalUserData);
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
