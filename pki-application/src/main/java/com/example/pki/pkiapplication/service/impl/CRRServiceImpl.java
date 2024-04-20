package com.example.pki.pkiapplication.service.impl;

import com.example.pki.pkiapplication.model.CRR;
import com.example.pki.pkiapplication.repository.CRRRepository;
import com.example.pki.pkiapplication.service.CRRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CRRServiceImpl implements CRRService {

    @Autowired
    private CRRRepository crrRepository;

    @Override
    public List<CRR> findAll() {
        return crrRepository.findAll();
    }

    @Override
    public CRR findOne(Long id) {
        return crrRepository.findById(id).orElse(null);
    }

    @Override
    public CRR save(CRR crr) {
        return crrRepository.save(crr);
    }

    @Override
    public void remove(CRR crr) {
        crrRepository.delete(crr);
    }
}
