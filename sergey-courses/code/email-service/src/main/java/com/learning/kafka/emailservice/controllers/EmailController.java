package com.learning.kafka.emailservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send-email")
public class EmailController {

    @GetMapping("/success")
    public ResponseEntity<String> sendEmailSuccess(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("email sent successfully");
    }

    @GetMapping("/failed/{statusExpected}")
    public ResponseEntity<String> sendEmailSuccess(@PathVariable int statusExpected){
        return ResponseEntity.status(statusExpected).body("email failed");
    }
}
