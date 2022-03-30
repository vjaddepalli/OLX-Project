package com.zensar.olx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author DC65846
 *	This config file is needed for customizing CORS config.
 *	CORS-Cross Origin Resource Sharing.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("GET","DELETE","PUT","POST","OPTIONS")
			.allowedOrigins("http://localhost:4200");
	}

}
