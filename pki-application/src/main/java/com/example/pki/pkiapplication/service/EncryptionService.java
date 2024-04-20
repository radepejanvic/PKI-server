package com.example.pki.pkiapplication.service;

import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public interface EncryptionService {

    byte[] encrypt(byte[] data, PrivateKey key);

    byte[] decrypt(byte[] cipher, PublicKey key);

}
