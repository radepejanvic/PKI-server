package com.example.pki.pkiapplication.service.impl;

import com.example.pki.pkiapplication.model.User;
import com.example.pki.pkiapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> ret = userRepository.findByEmail(email);
        if (!ret.isEmpty() ) {
            return org.springframework.security.core.userdetails.User.withUsername(email).password(ret.get().getPassword()).roles(ret.get().getUserType().toString()).build();
        }
        throw new UsernameNotFoundException("User not found with this username: " + email);
    }
}
