package com.citybus.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CitybusTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CitybusTrackerApplication.class, args);
	}

}
