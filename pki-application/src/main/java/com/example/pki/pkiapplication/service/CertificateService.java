package com.example.pki.pkiapplication.service;

import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.Certificate;
import org.springframework.stereotype.Service;

public interface CertificateService extends JpaService<Certificate> {

    Certificate findByAlias(String alias);

}
