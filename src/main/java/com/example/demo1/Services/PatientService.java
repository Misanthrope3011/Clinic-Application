package com.example.demo1.Services;

import com.example.demo1.Entities.Patient;
import com.example.demo1.Entities.User;
import com.example.demo1.Repositories.PatientRepository;
import com.example.demo1.Repositories.UserRepository;
import com.example.demo1.dto.PatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public Page<Patient> getAllPatients(Integer page, Integer size) {
        return patientRepository.findAll(PageRequest.of(page, size));
    }

    public Patient createPatient(PatientDTO patient, Patient patientEntity) {
        User patientAccount = userRepository.findById(patient.getUserId()).orElse(null);
        patientEntity.setUser(patientAccount);
        patientEntity.setCity(patient.getCity());
        patientEntity.setName(patient.getName());
        patientEntity.setHomeNumber(patient.getHomeNumber());
        patientEntity.setPESEL(patient.getPESEL());
        patientEntity.setPostalCode(patient.getPostalCode());
        patientEntity.setStreet(patient.getStreet());
        patientEntity.setLastName(patient.getLastName());

        return patientRepository.save(patientEntity);
    }

    public boolean existsByPesel(String pesel) {
        return patientRepository.existsByPESEL(pesel);
    }

}
