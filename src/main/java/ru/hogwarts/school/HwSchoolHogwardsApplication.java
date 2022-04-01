package ru.hogwarts.school;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class HwSchoolHogwardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HwSchoolHogwardsApplication.class, args);
    }

}
