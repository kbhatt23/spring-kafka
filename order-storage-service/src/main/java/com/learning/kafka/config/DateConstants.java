package com.learning.kafka.config;


import java.time.format.DateTimeFormatter;

public interface DateConstants {

	String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

}
