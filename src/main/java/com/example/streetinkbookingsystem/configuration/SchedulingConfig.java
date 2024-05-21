package com.example.streetinkbookingsystem.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author Munazzah
 * @summary Class can remain empty. Purpose is to enable scheduling with
 * EnableScheduling to inactivate old clients via ClientService that has a scheduled method
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

}
