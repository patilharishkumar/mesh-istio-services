package com.een.services.cmusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CmUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmUsersApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplateBuilder()
//				.setReadTimeout(Duration.ofMillis(1000))
				.build();
	}
	
}
