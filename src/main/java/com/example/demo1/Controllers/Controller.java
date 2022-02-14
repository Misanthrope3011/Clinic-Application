package com.example.demo1.Controllers;

import com.example.demo1.*;
import com.example.demo1.DTOs.PatientDTO;
import com.example.demo1.EmailVerification.EmailSender;
import com.example.demo1.Entities.*;
import com.example.demo1.Enums.UserRole;
import com.example.demo1.JWT.JWToken;
import com.example.demo1.PDFGenerator.PDFWriter;
import com.example.demo1.Prototypes.Credentials;
import com.example.demo1.Prototypes.LoginResponse;
import com.example.demo1.Repositories.*;
import com.example.demo1.Services.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@Getter
@Setter
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

    EmailSender sender;
    AuthenticationManager authenticationManager;
    UserInfoService userInfoService;
    SampleRepository sampleRepository;
    TokenRepository tokenRepository;
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    PasswordEncoder bCryptPasswordEncoder;
    RoleRepository roleRepository;
    NewsRepository newsRepository;
    ContactFormService contactFormService;
    ExaminationService examinationService;
    SpecializationRepository specializationRepository;
    SampleRepository userRepository;
    MedicalProcedure medicalProcedure;
    VisitRepository visitRepository;
    UserDetailService userDetailService;
    PasswordEncoder encoder;
    JWToken token;
    PDFWriter writePdf;


    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getPageInfo() throws MessagingException {

        sender.sendMail("carrion.30.11@gmail.com", "przychodniahealthcare@gmail.com","Siema");
        return ResponseEntity.ok(userInfoService.findAllUsers());
    }

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

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody Credentials loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = token.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        switch (roles.get(0)) {
            case "ROLE_PATIENT":
                return ResponseEntity.ok(new LoginResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        userDetails.getPatient(),
                        roles,
                        userDetails.getImage()));
            case "ROLE_DOCTOR":
                return ResponseEntity.ok(new LoginResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        userDetails.getDoctor(),
                        roles,
                        userDetails.getImage()));
            case "ROLE_ADMIN":
                return ResponseEntity.ok(new LoginResponse(jwt, "ADMIN", roles));
        }
        return ResponseEntity.badRequest().body("Nie znaleziono odpowiedniej roli");
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody User signUpRequest) throws MessagingException {

          if (userRepository.existsByUsername(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email is already taken!"));
        }


        if(signUpRequest.getEmail() != null) {

            User user = new User(signUpRequest);
            user.setEncoded_password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()));
            user.setUsername(signUpRequest.getEmail());
            String strRoles = String.valueOf(signUpRequest.getUserRole());

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(UserRole.ROLE_PATIENT)
                        .orElseThrow(() -> new RuntimeException("Role is not found. 0"));

            } else {
                switch (strRoles) {
                    case "ROLE_ADMIN":
                        user.setUserRole(UserRole.ROLE_ADMIN);
                        break;
                    case "ROLE_DIRECTOR":
                        user.setUserRole(UserRole.ROLE_DIRECTOR);
                        break;
                    case "ROLE_PATIENT":
                        user.setUserRole(UserRole.ROLE_PATIENT);
                        break;
                    case "ROLE_DOCTOR":
                        user.setUserRole(UserRole.ROLE_DOCTOR);
                        break;
                    default:
                        return ResponseEntity.badRequest().body("User has to contain role");

                }
            }

            userRepository.save(user);
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60),
                    userRepository.findByUsername(user.getEmail()).orElse(null));
            tokenRepository.save(verificationToken);
            sender.sendMail(signUpRequest.getEmail(), "Rejestracja konta w przychodni","Dziekujęmy za aktywowanie" +
                            "konta w naszej klinice. Aby aktywowac konto \n" +
                    "<a href = http://localhost:8080/signUp?token=" + token + "> kliknij tutaj </a>"
                    );
            return ResponseEntity.ok(userDetailService.loadUserByUsername(user.getEmail()));
        }
        return ResponseEntity.ok("Email pojebalo");
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
    public ResponseEntity getPdfContent() throws DocumentException, IOException {

        writePdf.writePdf("XD");
        ClassPathResource pdfFile = new ClassPathResource("examination.pdf");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfFile.getInputStream()));
    }

    @GetMapping("/signUp")
    ResponseEntity<VerificationToken>  newUser(@RequestParam ("token") String token) {
        VerificationToken findUserToken = tokenRepository.findByToken(token);

        if(findUserToken == null) {
            return new ResponseEntity<VerificationToken>(findUserToken, HttpStatus.NOT_FOUND);
        }

        findUserToken.getUser().set_active(true);
        findUserToken.setConfirmationTime(LocalDateTime.now());
        return new ResponseEntity<VerificationToken>( findUserToken, HttpStatus.OK);
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
