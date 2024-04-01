package com.laba.ProductService.configuration;



import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableScheduling
public class GeneralConfiguration {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
