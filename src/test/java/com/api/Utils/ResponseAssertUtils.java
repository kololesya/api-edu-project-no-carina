package com.api.Utils;

import org.testng.Assert;

public class ResponseAssertUtils {

    public static void assertUserFieldsPresent(String responseBody) {
        Assert.assertTrue(responseBody.contains("name"), "Response does not contain 'name'");
        Assert.assertTrue(responseBody.contains("email"), "Response does not contain 'email'");
        Assert.assertTrue(responseBody.contains("gender"), "Response does not contain 'gender'");
        Assert.assertTrue(responseBody.contains("status"), "Response does not contain 'status'");
    }
}
