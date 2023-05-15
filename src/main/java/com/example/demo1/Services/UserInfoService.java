package com.example.demo1.Services;


import com.example.demo1.Entities.User;
import com.example.demo1.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserInfoService {

    private final UserRepository sampleRepository;

    public Optional<User> findById(Long id) {
        return sampleRepository.findById(id);
    }
    public User findUserById(Long id) {
        return sampleRepository.getById(id);
    }
    public List<User> findAllUsers() {
        return sampleRepository.findAll();
    }

}
