package com.backend.notariza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NotarizaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotarizaBackendApplication.class, args);
	}

}
