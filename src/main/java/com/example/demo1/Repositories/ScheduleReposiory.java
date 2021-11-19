package com.example.demo1.Repositories;

import com.example.demo1.Entities.WorkingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ScheduleReposiory extends JpaRepository<WorkingSchedule, Long> {

}
