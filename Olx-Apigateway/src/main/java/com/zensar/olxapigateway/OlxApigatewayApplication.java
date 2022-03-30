package com.zensar.olxapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OlxApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlxApigatewayApplication.class, args);
	}

}
