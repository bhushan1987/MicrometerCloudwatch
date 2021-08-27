package org.cv.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.cv")
public class ApplicationMain {
    public static void main(String[] args) {
        System.setProperty("aws.region", "us-east-1");
        SpringApplication.run(ApplicationMain.class, args);
    }
}
