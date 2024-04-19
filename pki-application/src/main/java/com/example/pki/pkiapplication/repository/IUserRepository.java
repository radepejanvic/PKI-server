package com.example.pki.pkiapplication.repository;

import com.example.pki.pkiapplication.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
}
