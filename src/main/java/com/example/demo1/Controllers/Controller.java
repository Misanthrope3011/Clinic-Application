package com.example.demo1.Controllers;

import com.example.demo1.*;
import com.example.demo1.DTOs.PatientDTO;
import com.example.demo1.Entities.*;
import com.example.demo1.Enums.UserRole;
import com.example.demo1.JWT.JWToken;
import com.example.demo1.Prototypes.Credentials;
import com.example.demo1.Prototypes.LoginResponse;
import com.example.demo1.Repositories.*;
import com.example.demo1.Services.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@CrossOrigin("http://localhost:4200")
public class Controller {

    @Autowired
    AuthenticationManager authenticationManager;
    UserInfoService userInfoService;
    SampleRepository sampleRepository;
    UserService userService;
    TokenRepository tokenRepository;
    PatientRepository patientRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    RoleRepository roleRepository;
    NewsRepository newsRepository;
    ContactFormService contactFormService;
    ExaminationService examinationService;

    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getPageInfo() {

        return ResponseEntity.ok(userInfoService.findAllUsers());
    }

    @GetMapping("/news")
     public ResponseEntity getNews(@RequestParam ("page") Integer page, @RequestParam ("limit") Integer newsLimitOnSinglePage) {


        Integer size = newsRepository.findAll().size();

        return ResponseEntity.ok(size);
    }

    @Autowired
    SampleRepository userRepository;

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWToken jwtUtils;

    @GetMapping("/prices")
    public ResponseEntity<HashMap<String, Double>> getExaminations() {
        return examinationService.getExaminations();
    }


    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody Credentials loginRequest) {
/*
c
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), grantedAuthority));

       SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);


        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
 */

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

                return ResponseEntity.ok(new LoginResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                       roles));

    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody User signUpRequest) {


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
            Set<Role> roles = new HashSet<>();

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
            return ResponseEntity.ok(userDetailService.loadUserByUsername(user.getEmail()));
        }
        return ResponseEntity.ok("Email pojebalo");
    }


    @PostMapping("/contact")
    ResponseEntity <ContactForm> saveContactForm(@RequestBody ContactForm contactForm) {

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

    @GetMapping("/savePatient")
    public ResponseEntity<List<Patient>> hello() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @GetMapping("/signUp")
    ResponseEntity<VerificationToken>  newUser(@RequestParam ("token") String token) {
        VerificationToken findUserToken = tokenRepository.findByToken(token);

        if(findUserToken == null) {
            return new ResponseEntity<VerificationToken>(findUserToken, HttpStatus.NOT_FOUND);
        }

        findUserToken.getUser().set_active(true);

        return new ResponseEntity<VerificationToken>( findUserToken, HttpStatus.OK);
    }


    @GetMapping("/findAll")
    List<User> findAll() {
        return sampleRepository.findAll();
    }
}
