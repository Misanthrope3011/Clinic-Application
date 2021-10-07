package com.example.demo1.Controllers;

import com.example.demo1.Entities.Doctor;
import com.example.demo1.Repositories.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@AllArgsConstructor
public class DoctorController {

    private DoctorRepository doctorRepository;

    @GetMapping("doctor/welcome")
    public List<Doctor> response() {

        return doctorRepository.findAll();
    }



}
