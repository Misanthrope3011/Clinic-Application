package com.example.demo1.Repositories;

import com.example.demo1.Entities.MedicalVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<MedicalVisit, Long> {
    Page<MedicalVisit> findAll(Pageable pageable);

}
