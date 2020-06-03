package com.acme.marsrover.service;

import org.joda.time.DateTime;

public interface DateConversionService {
    DateTime convertDateStringToDateTime(String sDate);
}
