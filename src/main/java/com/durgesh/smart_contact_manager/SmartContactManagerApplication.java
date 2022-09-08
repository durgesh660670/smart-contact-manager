package com.durgesh.smart_contact_manager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartContactManagerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SmartContactManagerApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

		System.out.println("Started...................");

	}

}
