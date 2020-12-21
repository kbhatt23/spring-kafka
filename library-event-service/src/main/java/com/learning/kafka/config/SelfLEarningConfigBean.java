package com.learning.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "self.learning")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelfLEarningConfigBean {

	private String name;
	private Integer age;
	private Double salary;
	
	
}
