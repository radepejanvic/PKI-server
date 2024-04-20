package com.example.pki.pkiapplication.service;

import java.security.PrivateKey;

public interface KeyStoringService {

    void write(PrivateKey key, String alias);

    PrivateKey read(String alias);

}
