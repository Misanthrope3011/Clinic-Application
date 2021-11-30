package com.example.demo1.VisitManager;

import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Entities.WorkSchedule;
import com.example.demo1.Repositories.DoctorRepository;
import com.example.demo1.Repositories.MedicalVisitRepository;
import com.example.demo1.Repositories.WorkScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class SortingVisitDates {

    @Autowired
    private DoctorRepository doctorRepository;
    private WorkScheduleRepository workScheduleRepository;
    private MedicalVisitRepository medicalVisitRepository;

    private List<DateFormat> availableDates;
    private Doctor doctor;



    public boolean isDateAvailable(Long doctorId, LocalDateTime visitDate) {
        availableDates = new ArrayList<>();
        Doctor currentDoctor = doctorRepository.findById(doctorId).orElse(null);

        if(currentDoctor == null) {
            return false;
        } else {
            Calendar calendar = Calendar.getInstance();
           /* calendar.setTime(new Date(currentDoctor.getWork_schedule().get(0).getStart_hour()));
            DateFormat start = new SdepartmentimpleDateFormat("HH:mm");
            DateFormat end = new SimpleDateFormat("HH:mm");

            start.format(currentDoctor.getWork_schedule().get(0).getStart_hour());

            end.format(currentDoctor.getWork_schedule().get(0).getEnd_hour());
            */
        }

        ArrayList<MedicalVisit> getAllDoctorVisits = (ArrayList<MedicalVisit>) doctorRepository.findById((long)6).get().getPatient_visits();



        ArrayList<LocalDateTime> sortedDoctorSchedule = (ArrayList<LocalDateTime>) getAllDoctorVisits.stream().map(MedicalVisit::getStartDate).sorted().collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < sortedDoctorSchedule.size() - 1; i++) {
            LocalDateTime formattedVisitTime = LocalDateTime.parse(visitDate.toString(), formatter);

            if (!(sortedDoctorSchedule.get(i).compareTo(formattedVisitTime) <= 0 && sortedDoctorSchedule.get(i + 1).compareTo(formattedVisitTime) >= 0)) {
                return false;
            }
        }

        return true;
    }
}
