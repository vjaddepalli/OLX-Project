package com.zensar.olxeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class OlxEurekaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlxEurekaserverApplication.class, args);
	}

}
