package com.example.pki.pkiapplication.service;

import com.example.pki.pkiapplication.model.Certificate;

public interface ValidatingService {

    boolean isValid(Certificate certificate);
}
