package com.example.pki.pkiapplication.service.impl;

import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.service.ValidatingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
public class ValidatingServiceImpl implements ValidatingService {

    @Override
    public boolean isValid(Certificate certificate){
        Long currentMillis = System.currentTimeMillis();
        while(certificate.getIssuer() != null){
            if(!(certificate.getIssuedOn() < currentMillis && certificate.getExpiresOn() > currentMillis)){
                return false;
            }
            certificate = certificate.getIssuer();
        }

        return certificate.getIssuedOn() < currentMillis && certificate.getExpiresOn() > currentMillis;
    }
}
