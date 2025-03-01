package com.globetrotter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

@SpringBootApplication
@EntityScan("com.globetrotter.model")
public class GlobetrotterApplication {
	public static void main(String[] args) {
		SpringApplication.run(GlobetrotterApplication.class, args);
	}
}
