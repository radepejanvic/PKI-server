package com.example.pki.pkiapplication.controller;


import com.example.pki.pkiapplication.service.KeyStoringService;
import com.example.pki.pkiapplication.util.RSAKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.KeyPair;
import java.security.PrivateKey;

@Controller
@Validated
@RequestMapping("/api/keys-test")
public class KeyPairController {

    @Autowired
    private KeyStoringService keyStoringService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(){

        RSAKeyGenerator generator = new RSAKeyGenerator();
        KeyPair keys = generator.generateKeys();

        keyStoringService.write(keys.getPrivate(), "test-key");
        PrivateKey key = keyStoringService.read("test-key");

        return new ResponseEntity<>(key, HttpStatus.OK);
    }

}
