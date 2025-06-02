package com.api.utils;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.List;

import org.testng.Assert;

import com.api.constants.HttpStatus;

public class GraphQLResponseValidator {

    public static void validateUserData(Map<String, String> expected, Map<String, String> actual) {
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = actual.get(key);
            Assert.assertEquals(actualValue, expectedValue, "Mismatch in field: " + key);
        }
    }

    public static void validateGetAllUsersResponse(HttpResponse<String> response) {
        Assert.assertEquals(response.statusCode(), HttpStatus.OK.getCode(), "Expected status code 200 for successful GraphQL query");
        String body = response.body();
        Assert.assertFalse(body.contains("\"errors\""), "Response contains GraphQL errors");
        List<String> expectedFields = List.of("nodes", "id", "name", "email", "gender", "status");
        for (String field : expectedFields) {
            Assert.assertTrue(body.contains(field), "Response should contain '" + field + "'");
        }
    }
}
