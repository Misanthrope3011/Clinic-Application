package com.example.demo1.Repositories;

import com.example.demo1.Entities.DoctorRatings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatesRepository extends JpaRepository<DoctorRatings, Long> {

}
