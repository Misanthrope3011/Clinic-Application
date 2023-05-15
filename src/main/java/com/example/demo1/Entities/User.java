package com.example.demo1.Entities;

import com.example.demo1.Enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "usr_account")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String encodedPassword;
    private String signUpDate;
    private UserRole userRole;
    private boolean isExpired;
    private boolean isActive = true;

    @NotNull
    @Size(min = 5, max = 30, message = "Email must have at least 5 characters and maximum 30 characters")
    private String email;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Patient patient;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Doctor doctor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Lob
    private byte[] image;

    public User(User user) {
        this.email = user.email;
        this.isActive = user.isActive;
        this.encodedPassword = user.encodedPassword;
        this.signUpDate = user.signUpDate;
        this.username = user.username;
        this.isExpired = user.isExpired;
        this.id = user.id;
        this.userRole = user.userRole;
    }


    public User() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> grantedAuthorityArrayList = new ArrayList<>();
        grantedAuthorityArrayList.add(new SimpleGrantedAuthority(userRole.name()));

        return grantedAuthorityArrayList;
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
