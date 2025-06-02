package com.api;

import java.net.http.HttpResponse;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.constants.JsonConstant;
import com.api.models.PostFactory;
import com.api.services.PostService;
import com.api.utils.TemplateUtils;
import static com.api.constants.HttpStatus.CREATED;
import static com.api.models.UserFactory.createDefaultUserDataWithRandomEmail;

public class PostTests extends BaseApiTest {

    @Test
    public void createValidPostTest() {
        Map<String, String> originalUserData = createDefaultUserDataWithRandomEmail ();
        int userId = userService.createUserAndReturnId(originalUserData);
        String postTemplate = TemplateUtils.loadTemplate(JsonConstant.POST_TEMPLATE_PATH);
        Map<String, String> postData = PostFactory.createPostData(userId);
        String postBody = TemplateUtils.populateTemplate(postTemplate, postData);
        PostService postService = new PostService();
        HttpResponse<String> postResponse = postService.createPost(postBody);
        Assert.assertEquals(postResponse.statusCode(), CREATED.getCode());
    }
}
