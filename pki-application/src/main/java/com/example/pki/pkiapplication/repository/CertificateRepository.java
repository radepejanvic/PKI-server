package com.example.pki.pkiapplication.repository;

import com.example.pki.pkiapplication.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Certificate findByAlias(String alias);

    List<Certificate> findByIssuerId(Long id);
}
