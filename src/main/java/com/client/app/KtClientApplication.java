package com.client.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.client.service.AcService;

@SpringBootApplication
@MapperScan(basePackages = {"com.client.mapper"})
@ComponentScan(basePackages = {"com.client"})
public class KtClientApplication implements CommandLineRunner{
	@Autowired
	AcService service;
	public static void main(String[] args) {
		SpringApplication.run(KtClientApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		service.start();	
	}
}