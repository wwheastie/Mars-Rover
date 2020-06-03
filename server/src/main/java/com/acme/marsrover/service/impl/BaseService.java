package com.acme.marsrover.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${dates.file.name}")
    protected String datesFileName;
}
