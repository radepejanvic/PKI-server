package com.example.pki.pkiapplication.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CRRDTO {

    private Long id;

    private BigInteger serialNumber;

    private String reason;

}
