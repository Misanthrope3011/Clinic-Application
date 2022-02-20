package com.example.demo1.Controllers;

import com.example.demo1.EmailVerification.EmailSender;
import com.example.demo1.Entities.Role;
import com.example.demo1.Entities.User;
import com.example.demo1.Entities.VerificationToken;
import com.example.demo1.Enums.UserRole;
import com.example.demo1.JWT.JWToken;
import com.example.demo1.Prototypes.Credentials;
import com.example.demo1.Prototypes.LoginResponse;
import com.example.demo1.Repositories.RoleRepository;
import com.example.demo1.Repositories.SampleRepository;
import com.example.demo1.Repositories.TokenRepository;
import com.example.demo1.Services.UserDetailService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@Getter
@Setter
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    UserDetailService userDetailService;
    PasswordEncoder encoder;
    JWToken token;
    AuthenticationManager authenticationManager;
    PasswordEncoder bCryptPasswordEncoder;
    EmailSender sender;
    SampleRepository userRepository;
    TokenRepository tokenRepository;
    RoleRepository roleRepository;



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
        return ResponseEntity.badRequest().body("User has no matching role");
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody User signUpRequest) throws MessagingException {

        if (userRepository.existsByUsername(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(("Email is already taken!"));
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
        return new ResponseEntity("Zly email", HttpStatus.NOT_ACCEPTABLE);
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


}
