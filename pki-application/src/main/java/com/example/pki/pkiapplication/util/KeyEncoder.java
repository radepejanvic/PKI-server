package com.example.pki.pkiapplication.util;

import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Base64;

@Component
public class KeyEncoder {

    public String encodePublicKey(PublicKey publicKey) {
        byte[] publicKeyBytes = publicKey.getEncoded();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);
        StringBuilder pemFormatBuilder = new StringBuilder();
        pemFormatBuilder.append("-----BEGIN PUBLIC KEY-----\n");
        pemFormatBuilder.append(publicKeyBase64);
        pemFormatBuilder.append("\n-----END PUBLIC KEY-----");
        return pemFormatBuilder.toString();
    }



}
