package com.example.pki.pkiapplication.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import java.util.HashSet;

@Entity
@Data
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long serialNumber;

    private String subject;

    private Long issuedOn;

    private Long expiresOn;

    private boolean valid;

    @OneToMany(fetch = FetchType.LAZY)
    private HashSet<Extension> extensions;

    @ManyToOne(fetch = FetchType.LAZY)
    private Certificate issuer;

    @Enumerated(EnumType.STRING)
    private CertificateType type;
}
