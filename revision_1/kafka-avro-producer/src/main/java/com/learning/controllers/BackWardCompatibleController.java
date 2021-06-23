package com.learning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.avro.Avro01;
import com.learning.avro.BackWardCompatibleMessage;
import com.learning.dtos.BackWardDTO;
import com.learning.dtos.EmployeeDTO;

@RestController
@RequestMapping("/avro/backward")
public class BackWardCompatibleController {

	@Autowired
	KafkaTemplate<String, BackWardCompatibleMessage> template;
	
	private int count = 1;
	
	@PostMapping
	public ResponseEntity<BackWardDTO> publishEmployee(@RequestBody BackWardDTO dto){
		
		template.send("avro-backward-topic",new BackWardCompatibleMessage(dto.getFirstName(), dto.getLastName()));
		return new ResponseEntity<>(dto,HttpStatus.CREATED);
	}
	
}
