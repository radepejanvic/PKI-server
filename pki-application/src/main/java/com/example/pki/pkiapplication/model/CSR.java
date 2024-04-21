package com.example.pki.pkiapplication.model;

import jakarta.persistence.*;
import lombok.Data;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

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
