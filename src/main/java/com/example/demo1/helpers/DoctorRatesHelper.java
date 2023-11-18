package com.example.demo1.helpers;

import com.example.demo1.entity.Doctor;
import com.example.demo1.entity.DoctorRatings;
import com.example.demo1.entity.Patient;
import com.example.demo1.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Service
public class DoctorRatesHelper {

    public List<Double> getDoctorAverageRates(List<Doctor> doctorRatings) {
        List<Double> allRatings = new ArrayList<>();

        doctorRatings.forEach(doctor -> allRatings.add(doctor.getDoctorRatings()
                .stream()
                .mapToDouble(DoctorRatings::getRating)
                .average().orElse(0)));

        return allRatings;
    }

    public List<DoctorRatings> sortRatesByDoctorId(Patient patient) {
        List<DoctorRatings> sortedByDoctor = patient.getRates();
        sortedByDoctor.sort(sortRatesByDoctorId());

        return sortedByDoctor;

    }

    private Comparator<DoctorRatings> sortRatesByDoctorId() {
        return new Comparator<>() {
            @Override
            public int compare(DoctorRatings doctorRating1, DoctorRatings doctorRating2) {
                return doctorRating1.getDoctor().getId().compareTo(doctorRating2.getDoctor().getId());
            }
        };
    }

    public List<DoctorRatings> formRatesVector(List<DoctorRatings> sortedByDoctor, DoctorRepository doctorRepository) {
        List<Doctor> doctorsInSystem = doctorRepository.findAllByOrderByIdAsc();
        int i = 0;
        for (Doctor doctor : doctorsInSystem) {
            if (ratingDoesNotExistForDoctor(sortedByDoctor, doctor)) {
                sortedByDoctor.add(i, null);
            }
            i++;
        }

        return sortedByDoctor;
    }

    private boolean ratingDoesNotExistForDoctor(List<DoctorRatings> sortedByDoctor, Doctor doctor) {
        return sortedByDoctor.stream()
                .noneMatch(ratings -> ratings.getDoctor().getId().equals(doctor.getId()));
    }

}
