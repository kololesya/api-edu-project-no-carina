package com.api.utils;

import java.util.Map;
import com.api.constants.JsonConstant;
import com.api.models.UserFactory;

public class RequestBodyBuilderUtil {

    public static String buildUserRequestBody(Map<String, String> userData) {
        String template = TemplateUtils.loadTemplate(JsonConstant.USER_TEMPLATE_PATH);
        return TemplateUtils.populateTemplate(template, userData);
    }

    public static String buildUpdatedUserRequestBody(Map<String, String> baseUserData, String newName, String newStatus) {
        return buildUserRequestBody(
                UserFactory.createUpdatedUserData(baseUserData, newName, newStatus)
        );
    }
}
