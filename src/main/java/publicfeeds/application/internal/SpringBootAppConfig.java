/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application.internal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main class entry point and also configuration for Spring Boot.
 *
 * @author io
 */
@SpringBootApplication(scanBasePackages = "publicfeeds")
@EntityScan(basePackages = "publicfeeds")
public class SpringBootAppConfig {

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(SpringBootAppConfig.class, args);
	}
	
	/**
	 * Configuration for CORS.
	 * Accept request from all origins.
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*");
			}
		};
	}

}
