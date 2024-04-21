package com.example.pki.pkiapplication.service.impl;

import com.example.pki.pkiapplication.dto.CSRDTO;
import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.model.Issuer;
import com.example.pki.pkiapplication.repository.CertificateRepository;
import com.example.pki.pkiapplication.service.CertificateService;
import com.example.pki.pkiapplication.service.KeyStoringService;
import com.example.pki.pkiapplication.util.CertificateGenerator;
import com.example.pki.pkiapplication.util.KeyGenerator;
import com.example.pki.pkiapplication.util.KeyStoreReader;
import com.example.pki.pkiapplication.util.KeyStoreWriter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }

    @Override
    public Certificate findOne(Long id) {
        return certificateRepository.findById(id).orElse(null);
    }

    @Override
    public Certificate save(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Override
    public void remove(Certificate certificate) {
        certificateRepository.delete(certificate);
    }

    @Override
    public Certificate findByAlias(String alias) {
        return certificateRepository.findByAlias(alias);
    }
}
