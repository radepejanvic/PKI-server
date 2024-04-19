package com.example.pki.pkiapplication.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Extension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String type;
}
