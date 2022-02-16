package com.example.demo1.Controllers;

import com.example.demo1.*;
import com.example.demo1.DTOs.PatientDTO;
import com.example.demo1.Entities.*;
import com.example.demo1.PDFGenerator.PDFWriter;
import com.example.demo1.Repositories.*;
import com.example.demo1.Services.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@RestController
@Getter
@Setter
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

    UserInfoService userInfoService;
    SampleRepository sampleRepository;
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    NewsRepository newsRepository;
    ContactFormService contactFormService;
    ExaminationService examinationService;
    SpecializationRepository specializationRepository;
    SampleRepository userRepository;
    MedicalProcedure medicalProcedure;
    VisitRepository visitRepository;
    PDFWriter writePdf;

    @GetMapping("/getAllPatients")
    public ResponseEntity<Page<Patient>> getAllPatient(@RequestParam (value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam (value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(patientRepository.findAll(PageRequest.of(page, size)));
    }


    @GetMapping("/getSpecializationList")
    public ResponseEntity<List<Specialization>> getSpecializations() {
        return ResponseEntity.ok(specializationRepository.findAll());
    }

    @PostMapping("/fileUpload/{id}")
    ResponseEntity file(@RequestBody byte[] image, @PathVariable Long id){

        User user = sampleRepository.findById(id).orElse(null);
        if(user != null) {
            user.setImage(image);
            sampleRepository.save(user);
            return ResponseEntity.ok(image);
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Nie znaleziono usera"));
    }

    @GetMapping("/getMedicalProcedures/{id}")
    ResponseEntity<List<MedicalProcedures>> getProceudres(@PathVariable Long id) {

        if(id == null) {
            id = doctorRepository.findAll().get(Integer.parseInt(String.valueOf(id))).getId();
        }

        return ResponseEntity.ok(Objects.requireNonNull(doctorRepository.findById(id).
                orElse(null)).getDoctor_specialization().getProcedures());
    }
    
    @GetMapping("/news")
     public ResponseEntity getNews(@RequestParam ("page") Integer page, @RequestParam ("limit") Integer newsLimitOnSinglePage) {

        List<News> newsOnRequestedPage = new ArrayList<>();
        page = page - 1;

        Integer size = newsRepository.findAll().size();
        List<News> allNews = newsRepository.findAll();
        Collections.reverse(allNews);


        if(size > newsLimitOnSinglePage * (page + 1))
            newsOnRequestedPage =  allNews.subList(newsLimitOnSinglePage * page, newsLimitOnSinglePage * (page + 1));
        else    newsOnRequestedPage =  allNews.subList(newsLimitOnSinglePage * page, size);
        return ResponseEntity.ok(newsOnRequestedPage);
    }


    @GetMapping("/prices")
    public ResponseEntity<HashMap<String, Double>> getExaminations() {
        return examinationService.getExaminations();
    }

    @GetMapping("/retrieveImage/{id}")
    public ResponseEntity getImage(@PathVariable Long id) {
        User doctor = sampleRepository.findById(id).orElse(null);
        if(doctor != null)
            return ResponseEntity.ok(doctor);
        return ResponseEntity.badRequest().body(new MessageResponse("Unexpected Error"));
    }


    @PostMapping("/contact")
    ResponseEntity <ContactForm> saveContactForm(@RequestBody ContactForm contactForm) {
        contactForm.setDate(LocalDateTime.now());
        return contactFormService.addNewContactForm(contactForm);
    }

    @GetMapping("/currentLogged")
    public Principal user(Principal user) {
        return user;
    }


    @PostMapping("/savePatient")
    public ResponseEntity hello(@RequestBody PatientDTO patient) {

        if(patientRepository.existsByPESEL(patient.getPESEL())) {
            userRepository.deleteById((long) (userRepository.findAll().size() - 1));
            return new ResponseEntity("PESEL istnieje w bazie", HttpStatus.NOT_ACCEPTABLE);
        }

        Patient patientEntity = new Patient();
        patientEntity.setUser(sampleRepository.findById(patient.getUser_id()).orElse(null));
        patientEntity.setCity(patient.getCity());
        patientEntity.setName(patient.getName());
        patientEntity.setHome_number(patient.getHome_number());
        patientEntity.setPESEL(patient.getPESEL());
        patientEntity.setPostal_code(patient.getPostal_code());
        patientEntity.setStreet(patient.getStreet());
        patientEntity.setLast_name(patient.getLast_name());

        if(patientEntity.getUser() != null) {
            patientRepository.save(patientEntity);

            return new ResponseEntity<Patient>(patientEntity, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("Nie znaleziono powiazanego Usera");
    }

    @GetMapping(path ="/getPdf")
    public ResponseEntity getPdfContent() throws Exception {

        writePdf.writePdf(visitRepository.findAll().get(0));

        ClassPathResource pdfFile = new ClassPathResource("examination.pdf");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfFile.getInputStream()));
    }

    @PostMapping(path = "/saveProcedure/{id}")
    public ResponseEntity saveProcedure(@RequestBody MedicalProcedures procedure) {

        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }


    @GetMapping("/findAll")
    List<User> findAll() {
        return sampleRepository.findAll();
    }

    @GetMapping("/getDoctorList")
    ResponseEntity<List<Doctor>> getAll(){
        return ResponseEntity.ok(doctorRepository.findAll());
    }

    @GetMapping("/getImage/{id}")
     org.springframework.http.ResponseEntity<byte[]> getUser(@PathVariable Long id){
        return ResponseEntity.ok(sampleRepository.findById(id).orElse(null).getImage());
    }


}
