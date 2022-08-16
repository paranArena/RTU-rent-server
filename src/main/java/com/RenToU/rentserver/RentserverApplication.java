package com.RenToU.rentserver;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RentserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentserverApplication.class, args);
	}
	@Bean
	public Mapper dozerMapper() {
		return DozerBeanMapperBuilder.buildDefault();
	}
}