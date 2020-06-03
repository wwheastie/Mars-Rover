package com.acme.marsrover.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AppExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<Map> handleResponseStatusException(ResponseStatusException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(buildResponseBody(new Date().toString(),
                String.valueOf(e.getStatus().value()), e.getLocalizedMessage()), e.getStatus());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map> handleGeneralException(Exception e) {
        logger.error("Error: " + e);
        return new ResponseEntity<>(buildResponseBody(new Date().toString(),
                "500", "Internal Server Error"),
                null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, String> buildResponseBody(String timestamp,
                                                  String status, String message) {
        Map<String, String> errorResponseMap = new HashMap<>();
        errorResponseMap.put("Timestamp: ", timestamp);
        errorResponseMap.put("Status: ", status);
        errorResponseMap.put("Message: ", message);
        return errorResponseMap;
    }
}
