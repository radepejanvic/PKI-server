package com.example.pki.pkiapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bouncycastle.asn1.x500.X500Name;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
public class Issuer {

    private X500Name x500name;
    private PrivateKey privateKey;
    private PublicKey publicKey;
}
