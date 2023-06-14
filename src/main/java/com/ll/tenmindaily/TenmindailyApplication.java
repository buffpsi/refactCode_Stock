package com.ll.tenmindaily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TenmindailyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenmindailyApplication.class, args);
	}

}
