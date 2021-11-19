package com.example.demo1.Repositories;

import com.example.demo1.Entities.Department;
import javassist.bytecode.DeprecatedAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
