package com.example.pki.pkiapplication.repository;

import com.example.pki.pkiapplication.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Certificate findByAlias(String alias);
}
