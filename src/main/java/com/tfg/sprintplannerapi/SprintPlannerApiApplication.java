package com.tfg.sprintplannerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class SprintPlannerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprintPlannerApiApplication.class, args);
    }

}
