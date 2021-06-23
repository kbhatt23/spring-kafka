package com.learning.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.learning.avro.Avro01;
import com.learning.avro.Employee;
import com.learning.dtos.EmployeeDTO;

@Service
public class EmployeeAvroProducer {

	//to demonstrate error case also when publishing non empployee object
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	
	public void publishEmployee(EmployeeDTO employeeDTO) {
		Employee employee = new Employee(employeeDTO.getFirstName(), employeeDTO.getLastName(), employeeDTO.getMaritalStatus(),
				employeeDTO.getAge());
		kafkaTemplate.send("employees-topic",  employee);
	}
	
	public void publishBadEmployee(Avro01 item) {
		//avro01 is not valid json schema item assigned to employees-topic
		kafkaTemplate.send("employees-topic",  item);
	}
}
