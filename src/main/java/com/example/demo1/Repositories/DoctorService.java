package com.example.demo1.Repositories;

import com.example.demo1.Entities.Doctor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.print.Doc;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    public List<Doctor> filterDoctorsByLastNameLetter(char letter){

        List<Doctor> listByLetter = doctorRepository.findAll().stream()
                .filter(doc -> doc.getLast_name().substring(0, 1).equals(Character.toString(letter)))
                .collect(Collectors.toList());

        return listByLetter;
    }




}
