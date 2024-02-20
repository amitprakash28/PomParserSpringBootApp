package com.clouddefenseAI.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(info = @Info(
		title = "POM Parser Spring Boot App"
		, version = "1.0"
		, description = "POM Parser application using Spring boot"
		, termsOfService = "https://google.com"
		, contact = @Contact(email = "google.com")
		, license = @License(url = "google.com")
)
)
public class OpenApiSwagger {
}