package com.example.demo1.service;

import com.example.demo1.entity.MedicalVisit;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("Phase 2")
public class SmsSender {

    public static final String ACCOUNT_SID = System.getenv("TWILIO_KEY");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_SECRET");
    public static final String NUMBER_PREFIX = "+48";

    public SmsSender() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSms(String phone, MedicalVisit visitInfo) {

        String smsContent = "Przypomnienie o wizycie, która odbedzie sie " +
                visitInfo.getStartDate() +
                " " + "u doktora " +
                visitInfo.getDoctor().getName() +
                " " + visitInfo.getDoctor().getLastName();
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("(848) 420-9531"),
                        new com.twilio.type.PhoneNumber(visitInfo.getPatient().getPhone() != null ? NUMBER_PREFIX + visitInfo.getPatient().getPhone() : "0"),
                        smsContent)
                .create();
    }
}
