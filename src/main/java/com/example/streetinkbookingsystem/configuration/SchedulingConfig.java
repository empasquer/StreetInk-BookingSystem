package com.example.streetinkbookingsystem.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author Munazzah
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    //Class can remain empty. Purpose is to enable scheduling with
    //@EnableScheduling to inactivate old clients from ClientService
}
