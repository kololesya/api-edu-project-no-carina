package com.api.utils;

import java.util.Map;

import com.api.builders.UserBuilder;
import com.api.constants.JsonConstant;
import com.api.models.UserFactory;

public class RequestBodyBuilderUtil {

    public static String buildUserRequestBody(Map<String, String> userData) {
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        return TemplateUtils.populateTemplate(template, userData);
    }

    public static String buildDefaultUserRequestBody() {
        return buildUserRequestBody(UserFactory.createDefaultUserData());
    }

    public static String buildInvalidUserRequestBody() {
        return buildUserRequestBody(UserFactory.createInvalidUserData());
    }

    public static String buildUpdatedUserRequestBody(Map<String, String> baseUserData, String newName, String newStatus) {
        Map<String, String> updatedData = new UserBuilder()
                .withName(newName)
                .withStatus(newStatus)
                .build();
        baseUserData.forEach(updatedData::putIfAbsent);
        return buildUserRequestBody(updatedData);
    }
}
