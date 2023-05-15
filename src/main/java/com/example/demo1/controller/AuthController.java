package com.example.demo1.controller;

import com.example.demo1.Services.EmailSender;
import com.example.demo1.Entities.User;
import com.example.demo1.Entities.VerificationToken;
import com.example.demo1.Prototypes.Credentials;
import com.example.demo1.Repositories.RoleRepository;
import com.example.demo1.Repositories.UserRepository;
import com.example.demo1.Repositories.TokenRepository;
import com.example.demo1.Services.UserAuthenticationService;
import com.example.demo1.Services.UserDetailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@Getter
@Setter
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserAuthenticationService userAuthenticationService;
    private final UserDetailService userDetailService;
    private final EmailSender sender;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;

    @PostMapping("/signIn")
    public ResponseEntity<Object> signIn(@RequestBody Credentials loginRequest) {
        return userAuthenticationService.loginUser(loginRequest);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody User signUpRequest) throws MessagingException {

        if (userRepository.existsByUsername(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(("Email is already taken!"));
        }

        if(signUpRequest.getEmail() != null) {
            User requestUser = userAuthenticationService.createUserFromRequest(signUpRequest);
            String token = userAuthenticationService.createUserToken(requestUser);
            sender.sendMail(signUpRequest.getEmail(), "Rejestracja konta w przychodni","Dziekujęmy za aktywowanie" +
                    "konta w naszej klinice. Aby aktywowac konto \n" +
                    "<a href = http://localhost:8080/signUp?token=" + token + "> kliknij tutaj </a>"
            );
            return ResponseEntity.ok(userDetailService.loadUserByUsername(requestUser.getEmail()));
        }
        return new ResponseEntity("Zly email", HttpStatus.NOT_ACCEPTABLE);
    }




    @GetMapping("/signUp")
    ResponseEntity<Object>  newUser(@RequestParam ("token") String token) {
        VerificationToken findUserToken = tokenRepository.findByToken(token);

        if(findUserToken == null) {
            return new ResponseEntity<>(String.format("Resource with token {%s} not found", token), HttpStatus.NOT_FOUND);
        }

        findUserToken.getUser().setActive(true);
        findUserToken.setConfirmationTime(LocalDateTime.now());
        return new ResponseEntity<>( findUserToken, HttpStatus.OK);
    }

}
