package com.example.pki.pkiapplication.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CSR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // base64 encoded public key
    @Column(unique = true, nullable = false)
    private String publicKey;

    @Column(nullable = false)
    private String commonName;

    private String organization;

    private String country;

    private String email;

    @Enumerated(EnumType.STRING)
    private CertificateType template;

    @Enumerated(EnumType.STRING)
    private CSRStatus status;
}
