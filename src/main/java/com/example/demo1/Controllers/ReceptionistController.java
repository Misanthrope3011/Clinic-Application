package com.example.demo1.Controllers;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receptionist")
@CrossOrigin(origins = "http://localhost:4200")
public class ReceptionistController {
}
