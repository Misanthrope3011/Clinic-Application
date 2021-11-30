package com.example.demo1.Entities;

import com.example.demo1.Enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.*;


@Entity
@Getter
@Setter
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;
    String encoded_password;
    String sign_up_date;
    String email;
    boolean is_expired;
    boolean is_active;
    UserRole userRole;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();



    public User(User user) {
        this.email = user.email;
        this.is_active = user.is_active;
        this.encoded_password = user.encoded_password;
        this.sign_up_date = user.sign_up_date;
        this.username = user.username;
        this.is_expired = user.is_expired;
        this.id = user.id;
        this.userRole = user.userRole;
    }

    public User() {
        super();
    }

    @OneToOne (fetch = FetchType.LAZY, mappedBy = "user")
    private Patient patient;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Doctor doctor;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        ArrayList<GrantedAuthority> grantedAuthorityArrayList = new ArrayList<>();
        grantedAuthorityArrayList.add(new SimpleGrantedAuthority(userRole.name()));

        return grantedAuthorityArrayList;
    }

    @Override
    public String getPassword() {
        return encoded_password;
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
