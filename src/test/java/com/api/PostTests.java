package com.api;

import java.net.http.HttpResponse;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.constants.JsonConstant;
import com.api.models.PostFactory;
import com.api.services.PostService;
import com.api.services.UserService;
import com.api.utils.TemplateUtils;
import static com.api.constants.HttpStatus.CREATED;

public class PostTests {

    @Test
    public void createValidPostTest() {
        UserService userService = new UserService();
        int userId = userService.createUserAndReturnId();
        String postTemplate = TemplateUtils.loadTemplate(JsonConstant.POST_TEMPLATE_PATH);
        Map<String, String> postData = PostFactory.createPostData(userId);
        String postBody = TemplateUtils.populateTemplate(postTemplate, postData);
        PostService postService = new PostService();
        HttpResponse<String> postResponse = postService.createPost(postBody);
        Assert.assertEquals(postResponse.statusCode(), CREATED.getCode());
    }
}
