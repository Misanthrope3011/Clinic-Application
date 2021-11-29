package com.example.demo1.Repositories;

import com.example.demo1.Entities.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {
}
