package com.example.pki.pkiapplication.service;

import com.example.pki.pkiapplication.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService extends JpaService<User>{
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

}

