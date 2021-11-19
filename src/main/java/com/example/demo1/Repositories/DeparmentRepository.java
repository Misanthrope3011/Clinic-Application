package com.example.demo1.Repositories;

import com.example.demo1.Entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeparmentRepository extends JpaRepository<Department, Long> {
}
