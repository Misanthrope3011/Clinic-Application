package com.example.demo1.utils;

import com.example.demo1.Entities.Patient;
import com.example.demo1.Entities.User;
import com.example.demo1.dto.UserDTO;

public class EntityUtils {

    public static void updateUser(UserDTO user, User edited) {
        edited.getPatient().setName(user.getFirstName() != null ? user.getFirstName() : edited.getPatient().getName());
        edited.getPatient().setHomeNumber(user.getHomeNumber() != null ? user.getHomeNumber() : edited.getPatient().getHomeNumber());
        edited.getPatient().setPESEL(user.getPESEL() != null ? user.getPESEL() : edited.getPatient().getPESEL());
        edited.getPatient().setCity(user.getCity() != null ? user.getCity() : edited.getPatient().getCity());
        edited.getPatient().setStreet(user.getStreet() != null ? user.getStreet() : edited.getPatient().getStreet());
        edited.getPatient().setLastName(user.getLastName() != null ? user.getLastName() : edited.getPatient().getLastName());
        edited.getPatient().setPostalCode(user.getPostalCode() != null ? user.getPostalCode() : edited.getPatient().getLastName());
    }

    public static void updatePatientData(UserDTO user, Patient edited) {
        edited.setName(user.getFirstName() != null ? user.getFirstName() : edited.getName());
        edited.setHomeNumber(user.getHomeNumber() != null ? user.getHomeNumber() : edited.getHomeNumber());
        edited.setPESEL(user.getPESEL() != null ? user.getPESEL() : edited.getPESEL());
        edited.setCity(user.getCity() != null ? user.getCity() : edited.getCity());
        edited.setStreet(user.getStreet() != null ? user.getStreet() : edited.getStreet());
        edited.setLastName(user.getLastName() != null ? user.getLastName() : edited.getLastName());
        edited.setPostalCode(user.getPostalCode() != null ? user.getPostalCode() : edited.getPostalCode());
    }

}
