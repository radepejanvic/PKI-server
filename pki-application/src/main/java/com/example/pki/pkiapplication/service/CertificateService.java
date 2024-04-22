package com.example.pki.pkiapplication.service;

import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.Certificate;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CertificateService extends JpaService<Certificate> {

    Certificate findByAlias(String alias);

    List<Certificate> traverseSubtree(Certificate startNode);

}
