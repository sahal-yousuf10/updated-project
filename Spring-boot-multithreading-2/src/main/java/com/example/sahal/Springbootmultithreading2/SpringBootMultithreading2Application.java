package com.example.sahal.Springbootmultithreading2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringBootMultithreading2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMultithreading2Application.class, args);
	}

}
