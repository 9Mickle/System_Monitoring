package com.epam.system_monitoring;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@OpenAPIDefinition
public class SystemMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemMonitoringApplication.class, args);
    }

}
