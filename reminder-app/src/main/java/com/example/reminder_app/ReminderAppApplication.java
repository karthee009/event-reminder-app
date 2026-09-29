package com.example.reminder_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReminderAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReminderAppApplication.class, args);
		System.out.println("Reminder-App is started...");
	}
}