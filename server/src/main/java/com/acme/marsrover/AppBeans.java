package com.acme.marsrover;

import com.acme.marsrover.service.ProcessDateService;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class AppBeans {
    @Autowired
    ProcessDateService processDateService;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean("processDateServiceDateParser")
    public DateTimeFormatter getMarsRoverImageServiceDateParser() {
        //Create parsers for multiple date patterns
        DateTimeParser[] parsers = {
                DateTimeFormat.forPattern("MM/dd/yy").getParser(),
                DateTimeFormat.forPattern("MMMM dd, yyyy").getParser(),
                DateTimeFormat.forPattern("MMM-dd-yyyy").getParser()
        };
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .append(null, parsers).toFormatter();
        return formatter;
    }

    @Bean("processDateServiceThreadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(100);
        executor.setThreadNamePrefix("processDateThread-");
        return executor;
    }

    @PostConstruct
    public void startUp() {
        processDateService.process();
    }
}
