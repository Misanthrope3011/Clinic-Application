package com.example.demo1.Repositories;

import com.example.demo1.Entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, CrudRepository<Patient,Long> {

    Optional<Patient> findByPESEL(String PESEL);
    Boolean existsByPESEL(String PESEL);
    Page<Patient> findAll(Pageable pageable);
}
