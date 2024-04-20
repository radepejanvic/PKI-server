package com.example.pki.pkiapplication.service.impl;

import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.repository.CertificateRepository;
import com.example.pki.pkiapplication.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
