package com.example.currencyconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CurrencyconverterApplication {

	public static void main(String[] args) {

		SpringApplication.run(CurrencyconverterApplication.class, args);
	}

}
