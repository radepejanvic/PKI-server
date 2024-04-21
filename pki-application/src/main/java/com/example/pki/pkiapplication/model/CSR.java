package com.example.pki.pkiapplication.model;

import com.example.pki.pkiapplication.model.enums.CSRStatus;
import com.example.pki.pkiapplication.model.enums.CertificateType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CSR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // base64 encoded public key
    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
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

    private String issuerAlias;

    private String subjectAlias;
}
