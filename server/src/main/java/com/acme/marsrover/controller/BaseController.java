package com.acme.marsrover.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final Map<String, String> defaultResponse() {
        return response("success");
    }

    protected final Map<String, String> response(String message) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message: ", message);
        return responseBody;
    }
}
