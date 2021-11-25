package com.example.demo1.Repositories;

import com.example.demo1.Entities.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<MedicalVisit, Long> {
}
