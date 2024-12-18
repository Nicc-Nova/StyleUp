package com.StyleUp.backend.services;

import com.StyleUp.backend.models.User;
import com.StyleUp.backend.models.UserPrincipal;
import com.StyleUp.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if (user != null) {
            return new UserPrincipal(user);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
