package com.example.demo1.Controllers;

import com.example.demo1.*;
import com.example.demo1.Entities.*;
import com.example.demo1.Enums.UserRole;
import com.example.demo1.JWT.JWToken;
import com.example.demo1.Prototypes.Credentials;
import com.example.demo1.Prototypes.LoginResponse;
import com.example.demo1.Repositories.*;
import com.example.demo1.Services.ContactFormService;
import com.example.demo1.Services.UserDetailService;
import com.example.demo1.Services.UserInfoService;
import com.example.demo1.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
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


    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getPageInfo() {

        return userInfoService.findAllUsers();
    }


    @GetMapping("/news")
     public ResponseEntity getNews(@RequestParam ("page") Integer page, @RequestParam ("limit") Integer newsLimitOnSinglePage) {


        Integer size = newsRepository.findAll().size();

        return ResponseEntity.ok(size);
    }

    ContactFormService contactFormService;

    @Autowired
    SampleRepository userRepository;

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWToken jwtUtils;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody Credentials loginRequest) {


      Collection <? extends GrantedAuthority> grantedAuthority = Collections.singleton(new SimpleGrantedAuthority("USER"));

       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), grantedAuthority));

       SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());


        return ResponseEntity.ok(new LoginResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody User signUpRequest) {

        signUpRequest.setUserRole(UserRole.ROLE_PATIENT);


      if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username is already taken!"));
        }

        User user = new User(signUpRequest);


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

            }
        }

        userRepository.save(user);

        return ResponseEntity.ok(signUpRequest);
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
    public ResponseEntity<Patient> hello(@RequestBody Patient patient) {
        return new ResponseEntity<>(patientRepository.save(patient), HttpStatus.OK);
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

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return sampleRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/findAll")
    List<User> findAll() {
        return sampleRepository.findAll();
    }
}
