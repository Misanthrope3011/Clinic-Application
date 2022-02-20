package com.example.demo1.Helpers;

import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.DoctorRatings;
import com.example.demo1.Entities.Patient;
import com.example.demo1.Repositories.DoctorRepository;
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


    public List<Double> getDoctorAverageRates(List <Doctor> doctor) {
        List<Double> allRatings = new ArrayList<>();

        doctor.forEach(e -> allRatings.add(e.getDoctor_ratings()
                .stream()
                .mapToDouble(lam -> lam.getRating())
                .average().orElse(0)));

        return allRatings;
    }

    public List<DoctorRatings> sortRatesByDoctorId(Patient patient) {
        List<DoctorRatings> sortedByDoctor = patient.getRatings_by_patient();

        sortedByDoctor.sort(new Comparator<DoctorRatings>() {
            @Override
            public int compare(DoctorRatings h1, DoctorRatings h2) {
                return h1.getDoctor().getId().compareTo(h2.getDoctor().getId());
            }
        });

        return sortedByDoctor;

    }

    public List<DoctorRatings> formRatesVector(List<DoctorRatings> sortedByDoctor, DoctorRepository doctorRepository) {

        for (int i = 0; i < doctorRepository.findAll().size(); i++) {
            boolean exists = false;
            for (int j = 0; j < sortedByDoctor.size(); j++) {
                if (sortedByDoctor.get(j).getDoctor().getId().equals(doctorRepository.findAll().get(i).getId())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                sortedByDoctor.add(i, null);
            }
        }

        return sortedByDoctor;
    }

}
