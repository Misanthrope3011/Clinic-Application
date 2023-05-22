package com.example.demo1.Repositories;

import com.example.demo1.Entities.MedicalProcedures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalProcedureRepository extends JpaRepository<MedicalProcedures, Long> {

}
