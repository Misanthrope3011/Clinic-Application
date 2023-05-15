package com.example.demo1.Services;

import com.example.demo1.Entities.Role;
import com.example.demo1.Entities.User;
import com.example.demo1.Entities.VerificationToken;
import com.example.demo1.Enums.UserRole;
import com.example.demo1.JWT.JWToken;
import com.example.demo1.Prototypes.Credentials;
import com.example.demo1.Prototypes.LoginResponse;
import com.example.demo1.Repositories.RoleRepository;
import com.example.demo1.Repositories.UserRepository;
import com.example.demo1.Repositories.TokenRepository;
import com.example.demo1.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JWToken token;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    public ResponseEntity<Object> loginUser(Credentials loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = token.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> userRoles =  collectRoles(userDetails);
        return getRoleBasedResponse(jwt, userDetails, userRoles);
    }

    public User createUserFromRequest(User signUpRequest) {
        User user = new User(signUpRequest);
        user.setEncodedPassword(encoder.encode(signUpRequest.getPassword()));
        user.setUsername(signUpRequest.getEmail());
        assignRoleToUser(user);
        createUserToken(user);

        return user;
    }

    private List<String> collectRoles(User userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private ResponseEntity<Object> getRoleBasedResponse(String jwt, User userDetails, List<String> roles) {
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

    private User assignRoleToUser(User user) {
        String strRoles = String.valueOf(user.getUserRole());
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(UserRole.ROLE_PATIENT)
                    .orElseThrow(() -> new RuntimeException("Role is not found."));
            user.setUserRole(userRole.getName());
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
                    throw new ApplicationException("No role provided");
            }
        }
        return userRepository.save(user);

    }

    public String createUserToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60),
                userRepository.findByUsername(user.getEmail()).orElse(null));
        tokenRepository.save(verificationToken);
        return token;
    }

}
