package com.api.utils;

import com.api.constants.JsonConstant;
import com.api.models.UserFactory;

import java.util.Map;

public class RequestBodyBuilderUtil {

    public static String buildUserRequestBody(Map<String, String> userData) {
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        return TemplateUtils.populateTemplate(template, userData);
    }

    public static String buildDefaultUserRequestBody() {
        return buildUserRequestBody(UserFactory.createDefaultUserData());
    }
}
