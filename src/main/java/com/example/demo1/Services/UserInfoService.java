package com.example.demo1.Services;


import com.example.demo1.Entities.User;
import com.example.demo1.Repositories.SampleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserInfoService {

    SampleRepository sampleRepository;

    public List<User> findAllUsers(){
        return sampleRepository.findAll();
    }

    public User findUserById(Long id) {
        return sampleRepository.getById(id);
    }

}
