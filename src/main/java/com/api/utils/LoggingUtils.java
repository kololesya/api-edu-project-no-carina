package com.api.utils;

import java.net.http.HttpResponse;

import org.slf4j.Logger;

public class LoggingUtils {

    public static void logRequestAndResponse(Logger logger, String method, String uri, String body, HttpResponse<String> response) {

        logger.info("[{}] Request to: {}", method, uri);
        logger.info("Request body: {}", body);
        logger.info("Status: {}, Response: {}", response.statusCode(), response.body());
    }
}
