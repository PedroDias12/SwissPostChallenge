package com.example.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class SpicePApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpicePApplication.class, args);
	}

}
