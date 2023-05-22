package com.example.demo1.repository;

import com.example.demo1.entity.MedicalProcedures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalProcedureRepository extends JpaRepository<MedicalProcedures, Long> {

}
