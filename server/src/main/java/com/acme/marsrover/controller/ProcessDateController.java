package com.acme.marsrover.controller;

import com.acme.marsrover.service.ProcessDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ProcessDateController extends BaseController {
    @Autowired
    private ProcessDateService processDateService;

    @GetMapping("/process/dates")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public Map processDates() {
        processDateService.process();
        return response("executing");
    }

    @GetMapping("/delete/images")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public Map clearDatabase() {
        processDateService.clearDatabase();
        return defaultResponse();
    }
}
