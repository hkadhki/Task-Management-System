package com.example.taskmanagersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TaskManagerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerSystemApplication.class, args);
    }

}
