package com.example.demo1.Services;

import com.example.demo1.Entities.User;
import com.example.demo1.Repositories.SampleRepository;
import com.example.demo1.User.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    SampleRepository sampleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = sampleRepository.findByUsername(username);

        return new UserPrincipal(user);
    }

}


