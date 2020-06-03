package com.acme.marsrover.service.impl;

import com.acme.marsrover.service.DateConversionService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DateConversionServiceImpl extends BaseService implements DateConversionService {
    @Autowired
    @Qualifier("processDateServiceDateParser")
    DateTimeFormatter dateTimeFormatter;

    /**
     * Converts a String date to a DateTime object
     *
     * @param sDate
     * @return
     */
    @Override
    public DateTime convertDateStringToDateTime(String sDate) {
        DateTime dateTime;
        try {
            logger.info("Parsing " + sDate + " to DateTime object");
            dateTime = dateTimeFormatter.parseDateTime(sDate);
            logger.info("Parsed date successfully");
        } catch (Exception e) {
            logger.warn("Error while parsing date " + sDate + ". Date will not be processed: ", e);
            return null;
        }
        return dateTime;
    }
}
