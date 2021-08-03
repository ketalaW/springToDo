package com.toDo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@SpringBootApplication
public class ToDoAppApplication{

	public static void main(String[] args) {
		SpringApplication.run(ToDoAppApplication.class, args);
	}

	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

}
