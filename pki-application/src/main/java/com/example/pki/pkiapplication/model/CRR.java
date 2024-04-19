package com.example.pki.pkiapplication.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CRR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Certificate certificate;

    private String reason;
}
