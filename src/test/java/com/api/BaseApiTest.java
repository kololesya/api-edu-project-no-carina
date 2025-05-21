package com.api;

import com.api.services.UserService;
import org.testng.annotations.BeforeMethod;

public class BaseApiTest {

    protected UserService userService;

    @BeforeMethod
    public void setUp() {
        userService = new UserService();
    }
}
