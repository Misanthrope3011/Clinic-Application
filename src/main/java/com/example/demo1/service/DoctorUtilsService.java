package com.example.demo1.service;


import com.example.demo1.entity.Doctor;
import com.example.demo1.entity.MedicalVisit;
import com.example.demo1.repository.DoctorRepository;
import com.example.demo1.repository.VisitRepository;
import com.example.demo1.dto.VisitDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DoctorUtilsService {

    private final DoctorRepository doctorRepository;
    private final VisitRepository visitRepository;

    private static LocalTime getVisitHourStartPoint(int requestedDay, Month requestedMonth) {
        LocalTime localTime;
        if (requestedDay == LocalDateTime.now().getDayOfMonth() && requestedMonth.equals(LocalDateTime.now().getMonth())) {
            localTime = LocalTime.of(LocalDateTime.now().getHour() + 1, LocalDateTime.now().getMinute() > 30 ? 0 : 30);
        } else {
            localTime = LocalTime.of(9, 0);
        }
        return localTime;
    }

    private static void generateVisitHours(List<LocalTime> listOfHours, LocalTime localTime) {
        while (localTime.getHour() < 17) {
            listOfHours.add(localTime);
            localTime = localTime.plusMinutes(30);
        }
    }

    public static TreeMap<String, List<MedicalVisit>> splitVisitsOnPendingAndHistory(Doctor doctor) {
        List<MedicalVisit> sortedByDate = doctor.getGetPatientVisits()
                .stream()
                .sorted(Comparator.comparing(MedicalVisit::getStartDate))
                .collect(Collectors.toList());

        return prepareMapWithPendingAndHistoryLabels(sortedByDate);
    }

    public static TreeMap<String, List<MedicalVisit>> prepareMapWithPendingAndHistoryLabels(List<MedicalVisit> sortedByDate) {
        boolean hasAnyPending = false;
        List<MedicalVisit> pending = new ArrayList<>();
        List<MedicalVisit> history = new ArrayList<>();

        for (int i = 0; i < sortedByDate.size(); i++) {
            if (sortedByDate.get(i).getStartDate().compareTo(LocalDateTime.now()) > 0) {
                history = sortedByDate.subList(0, i);
                pending = sortedByDate.subList(i, sortedByDate.size());
                hasAnyPending = true;
                break;
            }
        }

        if (!hasAnyPending) {
            history = sortedByDate;
        }

        TreeMap<String, List<MedicalVisit>> visitData = new TreeMap<>();
        visitData.put("Oczekujace", pending);
        visitData.put("Historia", history);
        return visitData;
    }

    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<LocalTime> getDoctorAvailableHours(VisitDTO visitDTO) {
        Doctor doctor = doctorRepository.findById(visitDTO.getDoctor()).orElse(null);
        List<LocalTime> hoursGivenDay = new ArrayList<>();
        List<LocalTime> listOfHours = new ArrayList<>();
        LocalTime localTime;

        int requestedDay = visitDTO.getDay().
                toInstant().atZone(ZoneId.systemDefault()).
                toLocalDate().getDayOfMonth();
        Month requestedMonth = visitDTO.getDay()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .getMonth();

        localTime = getVisitHourStartPoint(requestedDay, requestedMonth);
        generateVisitHours(listOfHours, localTime);
        prepareFreeAppointMentOnRequestedDay(visitDTO, doctor, hoursGivenDay, listOfHours);

        return listOfHours;
    }

    private void prepareFreeAppointMentOnRequestedDay(VisitDTO visitDTO, Doctor doctor, List<LocalTime> hoursGivenDay, List<LocalTime> listOfHours) {
        if (doctor != null) {
            hoursGivenDay = doctor.getGetPatientVisits().stream()
                    .filter(visit -> isVisitAppointedOnGivenDay(visit, visitDTO))
                    .sorted(Comparator.comparing(MedicalVisit::getStartDate))
                    .map(e -> e.getStartDate().toLocalTime())
                    .collect(toList());
        }

        for (LocalTime time : hoursGivenDay) {
            listOfHours.removeIf(temp -> temp.equals(time));
        }
    }

    private boolean isVisitAppointedOnGivenDay(MedicalVisit visit, VisitDTO visitDTO) {
        return isAppointmentDayPartMatches(visit, visitDTO) && isAppointmentMonthPartMatches(visit, visitDTO);
    }

    private boolean isAppointmentDayPartMatches(MedicalVisit visit, VisitDTO visitDTO) {
        return visit.getStartDate().getDayOfMonth() == visitDTO.getDay()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
    }

    private boolean isAppointmentMonthPartMatches(MedicalVisit visit, VisitDTO visitDTO) {
        return visit.getStartDate().getMonth().toString().equals(visitDTO.getDay()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().toString());
    }

    public List<MedicalVisit> getAbandonedVisits() {
        return visitRepository.findAll().stream()
                .filter(MedicalVisit::isDeleteRequest)
                .collect(Collectors.toList());
    }

    public static List<MedicalVisit> getTodaysVisits(Doctor doctor) {
        return doctor.getGetPatientVisits().stream()
                .filter(e -> e.getStartDate().getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
                .collect(Collectors.toList());
    }

    public Optional<Doctor> findDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

}
