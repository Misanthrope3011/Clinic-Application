package com.example.demo1.service;


import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
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
    public User saveUser(User userToSave) {
        return sampleRepository.save(userToSave);
    }

}
