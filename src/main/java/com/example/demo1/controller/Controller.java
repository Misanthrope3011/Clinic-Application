package com.example.demo1.controller;

import com.example.demo1.Entities.*;
import com.example.demo1.PDFGenerator.PDFWriter;
import com.example.demo1.Repositories.*;
import com.example.demo1.Services.*;
import com.example.demo1.dto.PatientDTO;
import com.example.demo1.exception.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@RestController
@Getter
@Setter
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

    private DoctorRepository doctorRepository;
    private ContactFormService contactFormService;
    private ExaminationService examinationService;
    private SpecializationRepository specializationRepository;
    private UserRepository userRepository;
    private MedicalProcedure medicalProcedure;
    private VisitRepository visitRepository;
    private PDFWriter writePdf;
    private DoctorUtilsService doctorUtilsService;
    private UserInfoService userInfoService;
    private PatientService patientService;
    private NewsService newsService;

    @GetMapping("/getAllPatients")
    public ResponseEntity<Page<Patient>> getAllPatient(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(patientService.getAllPatients(page, size));
    }

    @GetMapping("/getSpecializationList")
    public ResponseEntity<List<Specialization>> getSpecializations() {
        return ResponseEntity.ok(specializationRepository.findAll());
    }

    @PostMapping("/fileUpload/{id}")
    public ResponseEntity<Object> file(@RequestBody byte[] image, @PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApplicationException("Error fetching user"));
        user.setImage(image);
        userRepository.save(user);

        return ResponseEntity.ok(image);
    }

    @GetMapping("/getMedicalProcedures/{id}")
    public ResponseEntity<Object> getProceudres(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorUtilsService.findDoctorById(id);
        if (doctor.isPresent()) {
            return ResponseEntity.ok().body(doctor.get().getDoctorSpecialization().getProcedures());
        }

        return ResponseEntity.badRequest().body(String.format("Resource with id %s not found", id));
    }

    @GetMapping("/news")
    public ResponseEntity<Object> getNews(@RequestParam("page") Integer page, @RequestParam("limit") Integer newsLimitOnSinglePage) {
        List<News> newsOnRequestedPage;
        page = page - 1;
        Integer requestSize = newsService.getAllNews().size();
        List<News> allNews = newsService.getAllNews(); //TODO: rewrite custom pagination to jpa implementation
        newsService.reverseNews(allNews);
        newsOnRequestedPage = newsService.paginateNews(page, newsLimitOnSinglePage, requestSize, allNews);
        return ResponseEntity.ok(newsOnRequestedPage);
    }

    @GetMapping("/prices")
    public ResponseEntity<HashMap<String, Double>> getExaminations() {
        return examinationService.getExaminations();
    }

    @GetMapping("/retrieveImage/{id}")
    public ResponseEntity<Object> getImage(@PathVariable Long id) {
        Optional<User> user = userInfoService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        }
        return new ResponseEntity<>("Blad przy pobieraniu obrazu", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/contact")
    ResponseEntity<ContactForm> saveContactForm(@RequestBody ContactForm contactForm) {
        contactForm.setDate(LocalDateTime.now());
        return contactFormService.addNewContactForm(contactForm);
    }

    @GetMapping("/currentLogged")
    public Principal user(Principal user) {
        return user;
    }

    @PostMapping("/savePatient")
    public ResponseEntity hello(@RequestBody PatientDTO patient) {
        if (patientService.existsByPesel(patient.getPESEL())) {
            return new ResponseEntity<>("PESEL istnieje w bazie", HttpStatus.CONFLICT);
        }
        User patientAccount = userRepository.findById(patient.getUserId()).orElseThrow(() -> new ApplicationException("No user found"));
        Patient patientEntity = new Patient();
        patientEntity.setUser(patientAccount);
        patientService.createPatient(patient, patientEntity);

        return new ResponseEntity<>(patientEntity, HttpStatus.OK);
    }

    @GetMapping(path = "/getPdf")
    public ResponseEntity getPdfContent() throws Exception {
        writePdf.writePdf(visitRepository.findAll().get(0));
        ClassPathResource pdfFile = new ClassPathResource("examination.pdf");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfFile.getInputStream()));
    }

    @PostMapping(path = "/saveProcedure/{id}")
    public ResponseEntity saveProcedure(@RequestBody MedicalProcedures procedure) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getDoctorList")
    public ResponseEntity<List<Doctor>> getAll() {
        return ResponseEntity.ok(doctorUtilsService.findAllDoctors());
    }

    @GetMapping("/getImage/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        User userImage = userInfoService.findById(id).orElseThrow(() -> new ApplicationException("Error fetching data"));
        return ResponseEntity.ok(Objects.requireNonNull(userImage).getImage());
    }

}
