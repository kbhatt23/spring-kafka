package com.learning.processors;

import java.util.Collections;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learning.avro.Employee;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

//@Configuration
public class EmployeeStreamProcessor {

	//@Bean
	public KStream<String, Employee> modifyName(StreamsBuilder builder)
	{
		SpecificAvroSerde<Employee> employeeSerde = new SpecificAvroSerde<Employee>();
		Map<String, String> singletonMap = Collections.singletonMap("schema.registry.url", "http://localhost:8081");
		employeeSerde.configure(singletonMap, false);
		Serde<String> stringSerde = Serdes.String();
		KStream<String, Employee> through = builder.stream("employees-topic", Consumed.with(stringSerde,employeeSerde))
			.mapValues(employee -> {
				employee.setFirstName(employee.getFirstName().toString().toUpperCase());
				employee.setLastName(employee.getLastName().toString().toUpperCase());
				return employee;
			})
			.through("employee-transformed-topic",Produced.with(stringSerde, employeeSerde));
		;
		
		through.print(Printed.<String, Employee>toSysOut().withLabel("employeeStream: Final Stream"));
		return through;
	}
}
