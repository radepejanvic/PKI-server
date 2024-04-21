package com.example.pki.pkiapplication.model;

import com.example.pki.pkiapplication.model.enums.CertificateType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Entity
@Data
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private BigInteger serialNumber;

    @Column(unique = true)
    private String alias;

    // TODO: Remove subject, issuedOn, expiresOn, valid
    private String subject;

    private Long issuedOn;

    private Long expiresOn;

    private boolean valid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Certificate issuer;

    @Enumerated(EnumType.STRING)
    private CertificateType type;
}
