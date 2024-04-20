package com.example.pki.pkiapplication.service.impl;

import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.repository.CSRRepository;
import com.example.pki.pkiapplication.service.CSRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSRServiceImpl implements CSRService {

    @Autowired
    private CSRRepository csrRepository;

    @Override
    public List<CSR> findAll() {
        return csrRepository.findAll();
    }

    @Override
    public CSR findOne(Long id) {
        return csrRepository.findById(id).orElse(null);
    }

    @Override
    public CSR save(CSR csr) {
        return csrRepository.save(csr);
    }

    @Override
    public void remove(CSR csr) {
        csrRepository.delete(csr);
    }
}
