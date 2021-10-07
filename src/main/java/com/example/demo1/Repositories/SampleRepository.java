package com.example.demo1.Repositories;

import com.example.demo1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
