package com.api;

import com.api.services.GraphQLService;
import org.testng.annotations.BeforeMethod;

public class BaseGraphQLTest {

    protected GraphQLService graphQLService;

    @BeforeMethod
    public void initGraphQLService() {
        graphQLService = new GraphQLService();
    }
}
