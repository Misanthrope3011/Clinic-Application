package com.example.demo1.repository;

import com.example.demo1.entity.MedicalVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends JpaRepository<MedicalVisit, Long> {
    Page<MedicalVisit> findAll(Pageable pageable);

}
