package com.reporting.mocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication(scanBasePackages = "com.reporting" )
public class MocksApplication {
	public static void main(String[] args) {
		SpringApplication.run(MocksApplication.class, args);
	}
}
