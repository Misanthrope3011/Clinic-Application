package com.example.demo1.Controllers;

import com.example.demo1.Entities.User;
import com.example.demo1.Repositories.SampleRepository;
import com.example.demo1.UserNotFoundException;
import com.sun.mail.iap.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
@AllArgsConstructor
public class AdminController {

    @Autowired
    SampleRepository sampleRepository;

    @GetMapping("/hello")
    public ResponseEntity<String> entity() {
        return ResponseEntity.ok("witaj adminie");
    }



}
