package com.api.Utils;

import java.util.List;
import java.util.Map;

import org.testng.Assert;

import com.jayway.jsonpath.JsonPath;

public class ResponseAssertUtils {

    public static void assertUserDataMatches(String responseBody, Map<String, String> expectedData) {
        Assert.assertEquals(extractUserField(responseBody, "name"), expectedData.get("name"), "Name mismatch");
        Assert.assertEquals(extractUserField(responseBody, "email"), expectedData.get("email"), "Email mismatch");
        Assert.assertEquals(extractUserField(responseBody, "gender"), expectedData.get("gender"), "Gender mismatch");
        Assert.assertEquals(extractUserField(responseBody, "status"), expectedData.get("status"), "Status mismatch");
    }

    public static void assertAllUsersContainRequiredFields(String responseBody) {
        List<Map<String, Object>> users = JsonPath.read(responseBody, "$[*]");
        Assert.assertFalse(users.isEmpty(), "User list should not be empty");
        for (Map<String, Object> user : users) {
            Assert.assertTrue(user.containsKey("id"), "User does not contain 'id'");
            Assert.assertTrue(user.containsKey("name"), "User does not contain 'name'");
            Assert.assertTrue(user.containsKey("email"), "User does not contain 'email'");
            Assert.assertTrue(user.containsKey("gender"), "User does not contain 'gender'");
            Assert.assertTrue(user.containsKey("status"), "User does not contain 'status'");
        }
    }

    private static String extractUserField(String responseBody, String fieldName) {
        String jsonPath = "$['" + fieldName + "']";
        return JsonPath.read(responseBody, jsonPath);
    }
}
