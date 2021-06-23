package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.avro.Avro01;
import com.learning.dtos.EmployeeDTO;
import com.learning.producers.EmployeeAvroProducer;

@RestController
@RequestMapping("/avro/employees")
public class EmployeeAvroController {

	@Autowired
	private EmployeeAvroProducer employeeAvroProducer;
	
	private int count = 1;
	
	@PostMapping
	public ResponseEntity<EmployeeDTO> publishEmployee(@RequestBody EmployeeDTO dto){
		employeeAvroProducer.publishEmployee(dto);
		return new ResponseEntity<>(dto,HttpStatus.CREATED);
	}
	
	@GetMapping("/error")
	public String publishErrorEmployee(){
		employeeAvroProducer.publishBadEmployee(new Avro01("fake-"+count++, "n/a", false));
		return "jai shree ram says error employee";
	}
}
