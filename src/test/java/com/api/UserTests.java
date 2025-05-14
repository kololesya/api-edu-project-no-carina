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

public class UserTests {

    @Test
    public void createValidUserTest() {

        Map<String, String> userData = UserFactory.createDefaultUserData();
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        String requestBody = TemplateUtils.populateTemplate(template, userData);
        UserService userService = new UserService();
        HttpResponse<String> response = userService.createUser(requestBody);
        Assert.assertEquals(response.statusCode(), HttpStatus.CREATED.getCode());
        System.out.println("Created user: " + response.body());
    }
}
